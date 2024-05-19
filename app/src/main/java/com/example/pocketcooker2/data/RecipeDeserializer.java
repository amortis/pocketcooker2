package com.example.pocketcooker2.data;

import com.example.pocketcooker2.data.Product;
import com.example.pocketcooker2.data.Recipe;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class RecipeDeserializer implements JsonDeserializer<Recipe> {
    private Map<String, Product> productMap;

    public RecipeDeserializer(Map<String, Product> productMap) {
        this.productMap = productMap;
    }

    @Override
    public Recipe deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String id = jsonObject.get("_id").getAsString();
        String name = jsonObject.get("name").getAsString();
        String description = jsonObject.get("description").getAsString();
        String photo = jsonObject.get("photo").getAsString();

        JsonObject productsJson = jsonObject.getAsJsonObject("products");
        Map<Product, Double> products = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : productsJson.entrySet()) {
            Product product = productMap.get(entry.getKey());
            if (product == null) {
                throw new JsonParseException("Unknown product ID: " + entry.getKey());
            }
            Double quantity = entry.getValue().getAsDouble();
            products.put(product, quantity);
        }

        return new Recipe(id, name, products, description, photo);
    }
}
