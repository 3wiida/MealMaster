package com.ewida.mealmaster.data.repository.meals_repo;

import com.ewida.mealmaster.data.model.AreasResponse;
import com.ewida.mealmaster.data.model.CategoriesResponse;
import com.ewida.mealmaster.data.model.CategoryMealsResponse;
import com.ewida.mealmaster.data.model.IngredientsResponse;
import com.ewida.mealmaster.data.model.Meal;
import com.ewida.mealmaster.data.model.MealResponse;
import com.ewida.mealmaster.data.model.Plan;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;


public interface MealsRepository {
    Single<MealResponse> getRandomMeal();

    Single<CategoryMealsResponse> getCategoryMeals(String category);

    Single<CategoryMealsResponse> getAreaMeals(String area);

    Single<CategoryMealsResponse> getIngredientMeals(String ingredient);

    Single<MealResponse> getMealById(String id);

    Single<MealResponse> search(String query);

    Single<CategoriesResponse> getAllCategories();

    Single<AreasResponse> getAllAreas();

    Single<IngredientsResponse> getAllIngredients();

    Completable saveMeal(Meal meal);

    Completable unSaveMeal(Meal meal);

    Single<Boolean> isMealSaved(String mealId);

    Flowable<List<Meal>> getSavedMeals();

    Completable planMeal(Plan plan);

    Completable unPlanMeal(Plan plan);

    Flowable<List<Plan>> getPlanedMeals(String date);

    Completable syncUserData(String userId);

    Single<List<Plan>> getAllPlans();
}
