package com.contentparser.beans;

import com.common.entity.ProductData;
import com.common.entity.ProductDataTs;
import com.common.entity.RawData;
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
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class)
                .getAsJsonObject("data")
                .getAsJsonArray("products")
                .get(0).getAsJsonObject();

        int price = jsonObject
                .getAsJsonPrimitive("priceU").getAsInt() / 100;
        int priceWithSale = jsonObject
                .getAsJsonPrimitive("salePriceU").getAsInt() / 100;

        int quantity = 0;
        var stocksArray = gson.fromJson(
                jsonObject
                        .getAsJsonArray("sizes").get(0),
                JsonObject.class).getAsJsonArray("stocks");

        for (JsonElement element : stocksArray) {
            quantity += element.getAsJsonObject()
                    .getAsJsonPrimitive("qty").getAsInt();
        }
        return new ProductDataTs(new Date(), price, priceWithSale, quantity);
    }

    public ProductData getProductInfo(RawData rawData) {
        var productData = getProductInfoFromJson(rawData.getJson());

        if (productData.getName() != null) {
            productData.addInfo(productData);
        }
        else
            rawData.setState(State.PARSING_ERROR);

        return productData;
    }

    private ProductData getProductInfoFromJson(String json) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class)
                .getAsJsonObject("data");
        ProductData productData = gson.fromJson(jsonObject.getAsJsonObject("productInfo"), ProductData.class);
        jsonObject = jsonObject.getAsJsonArray("colors").get(0).getAsJsonObject();
        productData.setProductId(jsonObject.get("cod1S").getAsLong());

        return productData;
    }
}
