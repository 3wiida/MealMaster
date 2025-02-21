package com.ewida.mealmaster.data.repository;

import com.ewida.mealmaster.data.model.CategoryMealsResponse;
import com.ewida.mealmaster.data.model.MealResponse;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Query;

public interface MealsRepository {
    Single<MealResponse> getRandomMeal();

    Single<CategoryMealsResponse> getCategoryMeals( String category);

    Single<MealResponse> getMealById(String id);

    Single<MealResponse> search(String query);
}
