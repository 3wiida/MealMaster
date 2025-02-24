package com.ewida.mealmaster.main.saved;

import com.ewida.mealmaster.data.model.Meal;

import java.util.List;

public interface SavedMealsContracts {
    interface Presenter{
        void getSavedMeals();
        void unSaveMeal(Meal meal);
        void saveMeal(Meal meal);
        boolean isGuest();
    }

    interface View{
        void showSavedMeals(List<Meal> meals);
        void showEmptyState();
        void showMessage(String msg);
        void showUnsavedSnackBar(Meal meal);
    }
}
