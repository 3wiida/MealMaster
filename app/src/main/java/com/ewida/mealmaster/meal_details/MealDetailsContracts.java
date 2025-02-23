package com.ewida.mealmaster.meal_details;

import com.ewida.mealmaster.data.model.Meal;


public interface MealDetailsContracts {
    interface Presenter{
        void getMealById(String id);
        void saveMeal(Meal meal);
        void unSaveMeal(Meal meal);
        void isMealSaved(String mealId);
    }

    interface View{
        void showMealDetails(Meal meal);
        void showMessage(String msg);
        void setSavedIcon(boolean isSaved);
    }
}
