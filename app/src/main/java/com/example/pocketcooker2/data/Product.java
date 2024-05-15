package com.example.pocketcooker2.data;

public class Product {
    private String _id;
    private String name;
    private String unitOfCalculating;
    private String icon;

    public Product(String name){
        this.name = name;
    }
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

    public String getUnitOfCalculating() {
        return unitOfCalculating;
    }

    public void setUnitOfCalculating(String unitOfCalculating) {
        this.unitOfCalculating = unitOfCalculating;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
