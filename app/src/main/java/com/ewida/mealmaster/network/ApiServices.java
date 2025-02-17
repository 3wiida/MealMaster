package com.ewida.mealmaster.network;

import com.ewida.mealmaster.model.pojo.CategoryMealsResponse;
import com.ewida.mealmaster.model.pojo.RandomMealResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiServices {

    @GET("random.php")
    Single<RandomMealResponse> getRandomMeal();

    @GET("filter.php")
    Single<CategoryMealsResponse> getCategoryMeals(
        @Query("c") String Category
    );
}
