package com.ewida.mealmaster.main.home;

import com.ewida.mealmaster.data.model.CategoryMeal;
import com.ewida.mealmaster.data.model.Meal;

import java.util.List;

public interface HomeContracts {
    interface Presenter {
        void getRandomMeal();

        void getVegetarianMeals();

        void getDesserts();

        void getUsername();
    }

    interface View {
        void showUsername(String username);

        void showRandomMeal(Meal meal);

        void showVegetarianMeals(List<CategoryMeal> meals);

        void showDesserts(List<CategoryMeal> desserts);

        void showErrorMessage(String errorMSg);
    }
}
