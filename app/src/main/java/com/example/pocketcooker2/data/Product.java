package com.example.pocketcooker2.data;

import java.util.Objects;

public class Product {
    private double quantity;
    private String _id;
    private String name;
    private String unitOfCalculating;
    private String icon;

    // Конструктор с двумя параметрами
    public Product(String name, double quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    // Конструктор с одним параметром
    public Product(String name) {
        this.name = name;
    }

    // Полный конструктор
    public Product(double quantity, String _id, String name, String unitOfCalculating, String icon) {
        this.quantity = quantity;
        this._id = _id;
        this.name = name;
        this.unitOfCalculating = unitOfCalculating;
        this.icon = icon;
    }

    // Конструктор без количества
    public Product(String _id, String name, String unitOfCalculating, String icon) {
        this._id = _id;
        this.name = name;
        this.unitOfCalculating = unitOfCalculating;
        this.icon = icon;
    }

    // Пустой конструктор
    public Product() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(name, product.name) &&
                Objects.equals(unitOfCalculating, product.unitOfCalculating) &&
                Objects.equals(icon, product.icon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, unitOfCalculating, icon);
    }

    // Геттеры и сеттеры
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

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
}
