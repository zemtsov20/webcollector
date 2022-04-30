package com.contentparser.scheduler;

import com.common.entity.ProductDataTs;
import com.common.entity.RawData;
import com.common.entity.ProductData;
import com.common.enums.State;
import com.common.repository.ProductDataTsRepository;
import com.common.repository.RawDataRepository;
import com.common.repository.ProductDataRepository;
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
import java.util.ArrayList;
import java.util.List;

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

    @Transactional
    @Scheduled(fixedDelay = 1000 * 5)
    public void getInfoFromRawData() {
        int count = 0;
        for (RawData rawData : rawDataRepo.findByState(State.DOWNLOADED, PageRequest.of(0, 5))) {
            String currentJson = rawData.getJson();
            String dataRef = rawData.getDataRef();
            if (currentJson.isEmpty()) {
                rawData.setState(State.PARSING_ERROR);
            }
            else {
                if (dataRef.contains("detail.aspx")) {
                    ProductData productData = productParse.getProductInfo(rawData);
                    productDataRepo.save(productData);
                }
                else if (dataRef.contains("api")) {
                    List<RawData> rawDataList;
                    if (currentJson.contains("currentMenu")
                            && !(rawDataList = getCategoriesList(currentJson, dataRef)).isEmpty()) {
                        logger.info("Added " + rawDataList.size() + " subcategories from " + rawData.getDataRef());
                    }
                    else {
                        rawDataList = getProductsFromJson(currentJson);
                        rawDataList.forEach(rawData1 -> rawData1.setParentRef(dataRef));
                        if (!dataRef.contains("page"))
                            rawDataRepo.saveAll(getCategoryPagesList(currentJson, dataRef));
                    }
                    count += rawDataList.size();
                    rawDataRepo.saveAll(rawDataList);
                } else {
                    ProductDataTs productDataTs = productParse.getProductTsInfo(currentJson);
                    productDataTs.setProductId(Long.parseLong(dataRef));
                    productDataTsRepo.save(productDataTs);
                }
                rawData.setState(State.PARSED);
            }
            rawDataRepo.save(rawData);
            logger.info(rawData.getDataRef() + " parsed, state: " + rawData.getState());
        }
        if (count > 0)
            logger.info(count + " products parsed successfully.");
    }

    private List<RawData> getCategoryPagesList(String json, String pageUrl) {
        List<RawData> rawDataList = new ArrayList<>();

        int totalPages = new Gson().fromJson(json, JsonObject.class)
                .getAsJsonObject("data")
                .getAsJsonObject("pager")
                .get("totalPages")
                .getAsInt();

        for (int i = 2; i <= 100 && i <= totalPages; i++) {
            rawDataList.add(new RawData(pageUrl + "?page=" + i, State.QUEUED));
        }

        return rawDataList;
    }

    private List<RawData> getCategoriesList(String json, String url) {
        List<RawData> rawDataList = new ArrayList<>();

        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(json, JsonObject.class)
                .getAsJsonObject("data")
                .getAsJsonArray("currentMenu");


        if (jsonArray != null && jsonArray.size() != 0) {
            for (JsonElement jsonElement : jsonArray) {
                String newUrl = gson.fromJson(jsonElement, JsonObject.class)
                        .get("url")
                        .getAsString();
                if (newUrl.contains(url) && !newUrl.equals(url))
                    rawDataList.add(new RawData(newUrl, State.QUEUED));
            }
        }

        return rawDataList;
    }

    private List<RawData> getProductsFromJson(String json) {
        List<RawData> rawDataList = new ArrayList<>();

        JsonArray jsonArray = new Gson().fromJson(json, JsonObject.class)
                .getAsJsonObject("data")
                .getAsJsonArray("products");
        if (jsonArray != null && jsonArray.size() != 0)
            jsonArray.forEach(productId -> rawDataList
                    .add(new RawData("/api/catalog/" + productId.getAsString() + "/detail.aspx", State.QUEUED)));

        return rawDataList;
    }

//    private List<ProductData> getProductsFromJson(String json, String categoryUrl) {
//        List<ProductData> productDataList = new ArrayList<>();
//
//        JsonArray jsonArray = new Gson().fromJson(json, JsonObject.class)
//                .getAsJsonObject("data")
//                .getAsJsonArray("products");
//        if (jsonArray != null && jsonArray.size() != 0)
//            jsonArray.forEach(product -> productDataList
//                    .add(new ProductData(product.getAsLong(), categoryUrl)));
//
//        return productDataList;
//    }
}
