package com.ewida.mealmaster.data.datasource.local.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ewida.mealmaster.data.model.Meal;
import com.ewida.mealmaster.data.model.Plan;

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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable planMeal(Plan plan);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void planMeals(List<Plan> plans);

    @Delete
    Completable unPlanMeal(Plan plan);

    @Query("SELECT EXISTS(SELECT 1 FROM SAVED_MEALS WHERE idMeal= :mealId and userId = :userId)")
    Single<Boolean> isMealSaved(String mealId, String userId);

    @Query("SELECT * FROM SAVED_MEALS WHERE userId = :userId")
    Flowable<List<Meal>> getSavedMeals(String userId);

    @Query("SELECT * FROM PLANS WHERE userId = :userId and date= :date")
    Flowable<List<Plan>> getPlanedMeals(String userId, String date);


}
