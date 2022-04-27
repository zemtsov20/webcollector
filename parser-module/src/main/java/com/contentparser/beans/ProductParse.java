package com.contentparser.beans;

import com.common.entity.ProductData;
import com.common.entity.ProductDataTs;
import com.common.enums.State;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Random;

@Component
public class ProductParse {

    public ProductDataTs getProductTsInfo(String json) throws InterruptedException {
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
        // temporary, need to fix
        // Thread.sleep(new Random().nextInt(50));
        return new ProductDataTs(new Date(), price, priceWithSale, quantity);
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
        JsonElement jsonObject = gson.fromJson(json, JsonObject.class)
                .getAsJsonObject("data")
                .getAsJsonArray("products")
                .get(0);

        return gson.fromJson(jsonObject, ProductData.class);
    }
}
