package com.ewida.mealmaster.main.home;

import com.ewida.mealmaster.model.pojo.CategoryMeal;
import com.ewida.mealmaster.model.pojo.Meal;

import java.util.List;

public interface HomeViewContract {
    void showRandomMeal(Meal meal);
    void showVegetarianMeals(List<CategoryMeal> meals);
    void showDesserts(List<CategoryMeal> desserts);
    void showErrorMessage(String errorMSg);
}
