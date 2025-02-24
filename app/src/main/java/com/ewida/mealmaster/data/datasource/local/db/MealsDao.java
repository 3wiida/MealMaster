package com.ewida.mealmaster.data.datasource.local.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.ewida.mealmaster.data.model.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface MealsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable saveMeal(Meal meal);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMeals(List<Meal> meals);

    @Delete
    Completable unSaveMeal(Meal meal);

    @Query("SELECT EXISTS(SELECT 1 FROM SAVED_MEALS WHERE idMeal= :mealId and userId = :userId)")
    Single<Boolean> isMealSaved(String mealId, String userId);

    @Query("SELECT * FROM SAVED_MEALS WHERE userId = :userId")
    Flowable<List<Meal>> getSavedMeals(String userId);
}
