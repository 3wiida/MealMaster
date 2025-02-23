package com.ewida.mealmaster;

import android.app.Application;

public class MealMasterApplication extends Application {
    private static MealMasterApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static MealMasterApplication getInstance() {
        return instance;
    }
}
