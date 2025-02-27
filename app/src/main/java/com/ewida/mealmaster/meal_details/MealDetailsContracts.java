package com.ewida.mealmaster.meal_details;

import com.ewida.mealmaster.data.model.Meal;
import com.ewida.mealmaster.data.model.Plan;


public interface MealDetailsContracts {
    interface Presenter {
        void getMealById(String id);

        void saveMeal(Meal meal);

        void unSaveMeal(Meal meal);

        void isMealSaved(String mealId);

        void planMeal(Meal meal, String date);

        boolean isGuest();
    }

    interface View {
        void showMealDetails(Meal meal);

        void showMessage(String msg);

        void setSavedIcon(boolean isSaved);
    }
}
