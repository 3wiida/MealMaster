package com.ewida.mealmaster.data.datasource.local;

import com.ewida.mealmaster.data.model.Meal;
import com.ewida.mealmaster.data.model.Plan;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface MealsLocalDataSource {
    Completable saveMeal(Meal meal);

    Completable saveMeals(List<Meal> meals);

    Completable unSaveMeal(Meal meal);

    Single<Boolean> isMealSaved(String mealId);

    Flowable<List<Meal>> getSavedMeals();

    Completable planMeal(Plan plan);

    Completable planMeals(List<Plan> plans);

    Completable unPlanMeal(Plan plan);

    Flowable<List<Plan>> getPlanedMeals(String date);

    Single<List<Plan>> getAllPlans();
}
