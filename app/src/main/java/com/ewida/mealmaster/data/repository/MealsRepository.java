package com.ewida.mealmaster.data.repository;

import com.ewida.mealmaster.data.model.AreasResponse;
import com.ewida.mealmaster.data.model.CategoriesResponse;
import com.ewida.mealmaster.data.model.CategoryMealsResponse;
import com.ewida.mealmaster.data.model.IngredientsResponse;
import com.ewida.mealmaster.data.model.MealResponse;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Query;

public interface MealsRepository {
    Single<MealResponse> getRandomMeal();
    Single<CategoryMealsResponse> getCategoryMeals( String category);
    Single<CategoryMealsResponse> getAreaMeals(String area);
    Single<CategoryMealsResponse> getIngredientMeals(String ingredient);
    Single<MealResponse> getMealById(String id);
    Single<MealResponse> search(String query);
    Single<CategoriesResponse> getAllCategories();
    Single<AreasResponse> getAllAreas();
    Single<IngredientsResponse> getAllIngredients();
}
