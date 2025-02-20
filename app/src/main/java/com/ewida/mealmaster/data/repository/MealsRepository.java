package com.ewida.mealmaster.data.repository;

import com.ewida.mealmaster.data.model.CategoryMealsResponse;
import com.ewida.mealmaster.data.model.MealResponse;
import io.reactivex.rxjava3.core.Single;

public interface MealsRepository {
    Single<MealResponse> getRandomMeal();

    Single<CategoryMealsResponse> getCategoryMeals( String category);

    Single<MealResponse> getMealById(String id);
}
