package com.ewida.mealmaster.model.pojo;

public class User {
    private  String id;
    private  String name;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
