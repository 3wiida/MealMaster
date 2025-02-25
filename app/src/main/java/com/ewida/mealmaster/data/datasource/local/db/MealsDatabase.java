package com.ewida.mealmaster.data.datasource.local.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.ewida.mealmaster.data.model.Meal;
import com.ewida.mealmaster.data.model.Plan;

@Database(entities = {Meal.class, Plan.class}, version = 1)
public abstract class MealsDatabase extends RoomDatabase {
    private static MealsDatabase instance;
    private static final String DATABASE_NAME = "MEAL_MASTER_DB";

    public abstract MealsDao getDao();

    public static synchronized MealsDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, MealsDatabase.class, DATABASE_NAME).build();
        }
        return instance;
    }
}
