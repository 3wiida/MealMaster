package com.ewida.mealmaster.data.datasource.remote.api;

import com.ewida.mealmaster.data.model.AreasResponse;
import com.ewida.mealmaster.data.model.CategoriesResponse;
import com.ewida.mealmaster.data.model.CategoryMealsResponse;
import com.ewida.mealmaster.data.model.IngredientsResponse;
import com.ewida.mealmaster.data.model.MealResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiServices {

    @GET("random.php")
    Single<MealResponse> getRandomMeal();

    @GET("filter.php")
    Single<CategoryMealsResponse> getCategoryMeals(@Query("c") String category);

    @GET("filter.php")
    Single<CategoryMealsResponse> getAreaMeals(@Query("a") String area);

    @GET("filter.php")
    Single<CategoryMealsResponse> getIngredientMeals(@Query("i") String ingredient);

    @GET("lookup.php")
    Single<MealResponse> getMealById(@Query("i") String id);

    @GET("search.php")
    Single<MealResponse> search(@Query("s") String query);

    @GET("categories.php")
    Single<CategoriesResponse> getAllCategories();

    @GET("list.php")
    Single<AreasResponse> getAllAreas(@Query("a") String list);

    @GET("list.php")
    Single<IngredientsResponse> getAllIngredients(@Query("i") String list);
}
