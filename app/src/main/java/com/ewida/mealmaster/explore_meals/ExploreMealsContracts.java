package com.ewida.mealmaster.explore_meals;

import com.ewida.mealmaster.data.model.CategoryMeal;

import java.util.List;

public interface ExploreMealsContracts {
    interface Presenter {
        void getMeals(ExploreType type, String query);
    }

    interface View {
        void showMealsList(List<CategoryMeal> meals);

        void showErrorMsg(String errorMsg);

        void hideLoadingState();
    }

    enum ExploreType {
        CATEGORY,
        AREA,
        INGREDIENT
    }
}
