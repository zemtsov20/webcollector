package com.contentparser.scheduler;

import com.common.entity.ProductData;
import com.common.entity.ProductDataTs;
import com.common.enums.State;
import com.common.repository.ProductDataRepository;
import com.google.gson.Gson;
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
import java.util.Date;

@Component
@RequiredArgsConstructor
public class ProductJsonParser {
    private static final Logger logger = LoggerFactory.getLogger(ProductJsonParser.class);

    @Autowired
    private ProductDataRepository productDataRepo;

    @Transactional
    @Scheduled(fixedDelay = 1000 * 60 * 60)
    public void getProductInfo() {
        int count = 0;
        for (ProductData productData : productDataRepo.findByState(State.DOWNLOADED, PageRequest.of(0, 10))) {
            productData.setState(State.PARSING);
            productDataRepo.save(productData);
            if (productData.getName() == null)
                productData = getProductInfo(productData);
            productData.getProductDataTs().add(getProductTsInfo(productData.getJson()));
            productDataRepo.save(productData);
        }
        logger.info(count + " products parsed successfully.");
    }

    private ProductDataTs getProductTsInfo(String json) {
        Gson gson = new Gson();
        JsonElement color = gson.fromJson(json, JsonObject.class)
                                .getAsJsonObject("data")
                                .getAsJsonArray("colors")
                                .get(0);
        JsonElement nomenclature = gson.fromJson(color, JsonObject.class)
                                       .getAsJsonArray("nomenclatures")
                                       .get(0);
        Integer price = gson.fromJson(nomenclature, JsonObject.class).get("rawMinPrice").getAsInt(),
                priceWithSale = gson.fromJson(nomenclature, JsonObject.class).get("rawMinPriceWithSale").getAsInt();

        return new ProductDataTs(new Date(), price, priceWithSale);
    }

    private ProductData getProductInfo(ProductData productData) {
        var temp = getProductInfoFromJson(productData.getJson());

        if (temp.getName() != null) {
            productData.addInfo(temp);
        }
        else
            productData.setState(State.PARSING_ERROR);

        return productDataRepo.save(productData);
    }

    private ProductData getProductInfoFromJson(String json) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class)
                                    .getAsJsonObject("data")
                                    .getAsJsonObject("productInfo");

        return gson.fromJson(jsonObject, ProductData.class);
    }
}
