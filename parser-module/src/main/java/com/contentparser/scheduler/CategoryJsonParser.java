package com.contentparser.scheduler;

import com.common.entity.CategoryData;
import com.common.entity.ProductData;
import com.common.enums.State;
import com.common.repository.CategoryDataRepository;
import com.common.repository.ProductDataRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryJsonParser {
    private static final Logger logger = LoggerFactory.getLogger(CategoryJsonParser.class);

    @Autowired
    private CategoryDataRepository categoryDataRepo;

    @Autowired
    private ProductDataRepository productDataRepo;

    @Transactional
    //@Scheduled(fixedDelay = 1000 * 60 * 60)
    public void getGoodsList() {
        int count = 0;
        for (CategoryData categoryData : categoryDataRepo.findByState(State.DOWNLOADED)) {
            categoryData.setState(State.PARSING);
            categoryDataRepo.save(categoryData);
            List<ProductData> productDataList = getProductsFromJson(categoryData.getJson(), categoryData.getPageUrl());
            if (productDataList.isEmpty()) {
                categoryData.setState(State.PARSED);
                categoryData.setPageToParse(1);
            }
            else {
                categoryData.setState(State.QUEUED);
                count += productDataList.size();
                productDataRepo.saveAll(productDataList);
                // incrementing page to parse new
                categoryData.incPage();
                categoryDataRepo.save(categoryData);
            }

            logger.info(categoryData.getPageUrl() + " parsed, state: " + categoryData.getState());
        }

        logger.info(count + " products parsed successfully.");
    }

    private List<ProductData> getProductsFromJson(String json, String categoryUrl) {
        List<ProductData> productDataList = new ArrayList<>();

        JsonArray jsonArray = new Gson().fromJson(json, JsonObject.class)
                                        .getAsJsonObject("data")
                                        .getAsJsonArray("products");
        if (jsonArray != null && jsonArray.size() != 0)
            jsonArray.forEach(product -> productDataList
                     .add(new ProductData(product.getAsLong(), categoryUrl)));

        return productDataList;
    }
}
