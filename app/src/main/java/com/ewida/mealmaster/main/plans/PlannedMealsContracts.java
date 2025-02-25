package com.ewida.mealmaster.main.plans;

import com.ewida.mealmaster.data.model.Meal;
import com.ewida.mealmaster.data.model.Plan;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;

public interface PlannedMealsContracts {
    interface presenter {
        void getPlannedMeals(String date);

        void unPlanMeal(Meal meal, String date);

        void planMeal(Plan plan);
    }

    interface View {
        void showPlannedMeals(List<Meal> meals);

        void showMessage(String msg);

        void showRemovedSnackBar(Plan plan);
    }
}
