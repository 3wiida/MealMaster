package com.ewida.mealmaster.meal_details;

import com.ewida.mealmaster.data.model.Meal;

public interface MealDetailsContracts {
    interface Presenter{
        void getMealById(String id);

    }

    interface View{
        void showMealDetails(Meal meal);
        void showErrorMessage(String errorMsg);
    }
}
