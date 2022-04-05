package com.contentparser.beans;

import com.common.entity.ProductData;
import com.common.entity.ProductDataTs;
import com.common.enums.State;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ProductParse {

    public ProductDataTs getProductTsInfo(String json) {
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

    public void getProductInfo(ProductData productData) {
        var temp = getProductInfoFromJson(productData.getJson());

        if (temp.getName() != null) {
            productData.addInfo(temp);
        }
        else
            productData.setState(State.PARSING_ERROR);
    }

    private ProductData getProductInfoFromJson(String json) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class)
                .getAsJsonObject("data")
                .getAsJsonObject("productInfo");

        return gson.fromJson(jsonObject, ProductData.class);
    }
}
