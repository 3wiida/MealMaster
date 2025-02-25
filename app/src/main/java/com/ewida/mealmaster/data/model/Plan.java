package com.ewida.mealmaster.data.model;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;


@Entity(tableName = "PLANS", primaryKeys = {"userId", "idMeal", "date"})
public class Plan {


    @NonNull
    private String date;

    @Embedded
    @NonNull
    private Meal meal;

    public Plan(@NonNull String date, @NonNull Meal meal) {
        this.date = date;
        this.meal = meal;
    }


    @NonNull
    public String getDate() {
        return date;
    }

    public void setDate(@NonNull String date) {
        this.date = date;
    }

    @NonNull
    public Meal getMeal() { return meal; }
    public void setMeal(@NonNull Meal meal) { this.meal = meal; }
}

