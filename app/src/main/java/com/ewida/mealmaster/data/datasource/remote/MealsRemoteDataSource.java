package com.ewida.mealmaster.data.datasource.remote;

import com.ewida.mealmaster.data.model.AreasResponse;
import com.ewida.mealmaster.data.model.CategoriesResponse;
import com.ewida.mealmaster.data.model.CategoryMealsResponse;
import com.ewida.mealmaster.data.model.IngredientsResponse;
import com.ewida.mealmaster.data.model.MealResponse;
import com.google.firebase.database.DatabaseReference;

import io.reactivex.rxjava3.core.Single;

public interface MealsRemoteDataSource {
    Single<MealResponse> getRandomMeal();
    Single<CategoryMealsResponse> getCategoryMeals(String category);
    Single<CategoryMealsResponse> getAreaMeals(String area);
    Single<CategoryMealsResponse> getIngredientMeals(String ingredient);
    Single<MealResponse> getMealById(String id);
    Single<MealResponse> search(String query);
    Single<CategoriesResponse> getAllCategories();
    Single<AreasResponse> getAllAreas();
    Single<IngredientsResponse> getAllIngredients();
    DatabaseReference getSavedMeals(String userId);
    DatabaseReference getPlannedMeals(String userId);
}
