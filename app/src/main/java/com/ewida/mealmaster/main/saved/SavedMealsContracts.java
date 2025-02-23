package com.ewida.mealmaster.main.saved;

import com.ewida.mealmaster.data.model.Meal;

import java.util.List;

public interface SavedMealsContracts {
    interface Presenter{
        void getSavedMeals();
        void unSaveMeal(Meal meal);
    }

    interface View{
        void showSavedMeals(List<Meal> meals);
        void showMessage(String msg);
    }
}
