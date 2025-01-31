package com.contentparser.scheduler;

import com.common.entity.*;
import com.common.enums.State;
import com.common.repository.*;
import com.contentparser.beans.ProductParse;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.*;

import static com.common.utils.Constants.wbApiPrefix;

/**
 * Main parsing class
 *
 */
@Component
@RequiredArgsConstructor
public class JsonParser {
    private static final Logger logger = LoggerFactory.getLogger(JsonParser.class);

    @Autowired
    private ProductParse productParse;

    @Autowired
    private RawDataRepository rawDataRepo;

    @Autowired
    private ProductDataRepository productDataRepo;

    @Autowired
    private ProductDataTsRepository productDataTsRepo;

    @Autowired
    private CategoryDataRepository categoryDataRepo;

    @Autowired
    private SiteDataRepository siteDataRepo;

    /**
     * Every 100ms method locks 1 row from database,
     * choosing parsing method and saves the result
     *
     */
    @Transactional
    @Scheduled(fixedDelay = 100)
    public void getInfoFromRawData() {
        for (RawData rawData : rawDataRepo.findAndLockByState(State.DOWNLOADED, PageRequest.of(0, 1))) {
            String currentJson = rawData.getJson();
            String dataRef = rawData.getDataRef();
            if (currentJson.isEmpty()) {
                rawData.setState(State.PARSING_ERROR);
            } else {
                if (dataRef.contains("getburger")) {
                    if (menuBurgerCheck(currentJson)) {
                        var siteDataList = siteDataRepo.findAll(PageRequest.of(0, 1)).toList();
                        SiteData siteData;
                        if (siteDataList.isEmpty())
                            siteData = new SiteData(new Date(), currentJson);
                        else {
                            siteData = siteDataList.get(0);
                            siteData.setJson(currentJson);
                            siteData.setDateTs(new Date());
                        }
                        siteDataRepo.save(siteData);
                        categoryDataRepo.saveAll(getAllCategoriesFromMenu(currentJson));
                        rawData.setState(State.PARSED);
                    } else
                        rawData.setState(State.PARSING_ERROR);
                } else if (dataRef.contains("detail.aspx")) {
                    ProductData productData = productParse.getProductInfo(currentJson);
                    if (productData.getName() != null) {
                        productData.setCategoryUrl(rawData.getParentRef());
                        productDataRepo.save(productData);
                        rawData.setState(State.PARSED);
                    } else
                        rawData.setState(State.PARSING_ERROR);
                } else if (dataRef.contains("api")) {
                    List<RawData> rawDataList;
                    // checking is category last node
                    if ((rawDataList = getCategoriesList(rawData.getDataRef())).isEmpty()) {
                        rawDataList = getProductsFromJson(currentJson, dataRef);
                        if (!dataRef.contains("page"))
                            rawDataRepo.saveAll(getCategoryPagesList(currentJson, dataRef));
                    } else {
                        logger.info("Added " + rawDataList.size() + " subcategories from " + rawData.getDataRef());
                    }
                    rawDataRepo.saveAll(rawDataList);
                    rawData.setState(State.PARSED);
                } else {
                    ProductDataTs productDataTs = productParse.getProductTsInfo(currentJson);
                    productDataTsRepo.save(productDataTs);
                    rawData.setState(State.PARSED);
                }
            }
            logger.info(rawData.getDataRef() + " parsed, state: " + rawData.getState());
            if (rawData.getState() == State.PARSED)
                rawDataRepo.delete(rawData);
        }
    }

    /**
     * Validation of JSON
     *
     * @param currentJson JSON in String format
     * @return Result of validation
     */
    private boolean menuBurgerCheck(String currentJson) {
        // TODO check JSON
        return true;
    }

    /**
     * Gets all pages of category
     *
     * @param json JSON in String format to parse
     * @param pageUrl Link to category
     * @return List of all category pages in RawData object
     */
    private List<RawData> getCategoryPagesList(String json, String pageUrl) {
        List<RawData> rawDataList = new ArrayList<>();

        int totalPages = new Gson().fromJson(json, JsonObject.class)
                .getAsJsonObject("data")
                .getAsJsonObject("pager")
                .get("totalPages")
                .getAsInt();

        for (int i = 2; i <= 100 && i <= totalPages; i++) {
            rawDataList.add(new RawData(pageUrl + "?page=" + i, pageUrl, State.QUEUED));
        }

        return rawDataList;
    }

    /**
     * Gets all subcategories from input category
     *
     * @param url Link to category
     * @return List of all subcategories in RawData object
     */
    private List<RawData> getCategoriesList(String url) {
        List<RawData> rawDataList = new ArrayList<>();
        CategoryData categoryData = categoryDataRepo
                .findTopByPageUrl(url.replace(wbApiPrefix, "")).orElse(null);
        if (categoryData != null && categoryData.hasChildren) {
            Queue<CategoryData> categoryDataQueue = new LinkedList<>();
            categoryDataQueue.add(categoryData);
            do {
                var temp = categoryDataQueue.remove();
                if (temp.hasChildren) {
                    categoryDataQueue.addAll(categoryDataRepo.findAllByParentId(temp.getId()));
                }
                else
                    rawDataList.add(new RawData(wbApiPrefix + temp.getPageUrl(), url, State.QUEUED));
            } while (!categoryDataQueue.isEmpty());
        }

        return rawDataList;
    }

    /**
     * Gets all products links from category page JSON
     *
     * @param json JSON in String format to parse
     * @param parentRef Link to category
     * @return List of all subcategories in RawData object
     */
    private List<RawData> getProductsFromJson(String json, String parentRef) {
        List<RawData> rawDataList = new ArrayList<>();

        JsonArray jsonArray = new Gson().fromJson(json, JsonObject.class)
                .getAsJsonObject("data")
                .getAsJsonArray("products");
        if (jsonArray != null && jsonArray.size() != 0)
            jsonArray.forEach(productId -> rawDataList
                    .add(new RawData(wbApiPrefix + "/api/catalog/"
                            + productId.getAsString() + "/detail.aspx", parentRef, State.QUEUED)));

        return rawDataList;
    }

    /**
     * Gets all categories from menu-burger JSON
     *
     * @param json JSON in String format to parse
     * @return List of all categories in CategoryData object
     */
    private List<CategoryData> getAllCategoriesFromMenu(String json) {
        List<CategoryData> dataList = new ArrayList<>();
        Gson gson = new Gson();
        Queue<JsonElement> queue = new LinkedList<>();
        gson.fromJson(json, JsonObject.class)
                .getAsJsonObject("data")
                .getAsJsonArray("catalog")
                .forEach(queue::add);

        while (!queue.isEmpty()) {
            JsonObject jsonObj = queue.remove().getAsJsonObject();
            JsonArray jsonArray = jsonObj.getAsJsonArray("childNodes");
            CategoryData categoryData = gson.fromJson(jsonObj, CategoryData.class);
            if (jsonArray.size() != 0) {
                categoryData.setHasChildren(true);
                jsonArray.forEach(queue::add);
            }
            else
                categoryData.setHasChildren(false);
            dataList.add(categoryData);
        }

        return dataList;
    }
}
