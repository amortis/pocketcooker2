package com.example.pocketcooker2;

import com.example.pocketcooker2.data.Product;

import java.util.HashMap;
import java.util.Map;


public class Recipe {
    private String _id;
    private String name;
    private Map<Product, Double> products;
    private String description;
    private String photo;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Product, Double> getProducts() {
        return products;
    }

    public void setProducts(Map<Product, Double> products) {
        this.products = products;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    // Additional method to add a product to the recipe
    public void addProduct(Product product, Double quantity) {
        if (products == null) {
            products = new HashMap<>();
        }
        products.put(product, quantity);
    }
}
