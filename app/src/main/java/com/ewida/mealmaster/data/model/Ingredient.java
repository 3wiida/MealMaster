package com.ewida.mealmaster.data.model;

public class Ingredient {
    private String thumbnail;
    private String name;
    private String quantity;

    public Ingredient(String thumbnail, String name, String quantity) {
        this.thumbnail = thumbnail;
        this.name = name;
        this.quantity = quantity;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getName() {
        return name;
    }

    public String getQuantity() {
        return quantity;
    }
}
