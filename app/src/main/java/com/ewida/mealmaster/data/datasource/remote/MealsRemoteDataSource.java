package com.ewida.mealmaster.data.datasource.remote;

import com.ewida.mealmaster.data.model.AreasResponse;
import com.ewida.mealmaster.data.model.CategoriesResponse;
import com.ewida.mealmaster.data.model.CategoryMealsResponse;
import com.ewida.mealmaster.data.model.IngredientsResponse;
import com.ewida.mealmaster.data.model.MealResponse;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Query;

public interface MealsRemoteDataSource {

    Single<MealResponse> getRandomMeal();

    Single<CategoryMealsResponse> getCategoryMeals(String category);

    Single<MealResponse> getMealById(String id);

    Single<MealResponse> search(@Query("s") String query);

    Single<CategoriesResponse> getAllCategories();

    Single<AreasResponse> getAllAreas();

    Single<IngredientsResponse> getAllIngredients();


}
