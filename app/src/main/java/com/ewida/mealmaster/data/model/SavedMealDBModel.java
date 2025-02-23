package com.ewida.mealmaster.data.model;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/*@Entity(tableName = "SAVED_MEALS")
public class SavedMealDBModel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String userId;
    @Embedded
    private Meal meal;

    public SavedMealDBModel(String userId, Meal meal) {
        this.userId = userId;
        this.meal = meal;
    }

    public int getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public Meal getMeal() {
        return meal;
    }

    public void setId(int id) {
        this.id = id;
    }
}*/
