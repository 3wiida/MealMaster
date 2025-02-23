package com.ewida.mealmaster.main.saved.presenter;

import android.annotation.SuppressLint;
import android.util.Log;

import com.ewida.mealmaster.data.model.Meal;
import com.ewida.mealmaster.data.repository.meals_repo.MealsRepository;
import com.ewida.mealmaster.data.repository.user_repo.UserRepository;
import com.ewida.mealmaster.main.saved.SavedMealsContracts;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SavedMealsPresenter implements SavedMealsContracts.Presenter {

    private final SavedMealsContracts.View view;
    private final MealsRepository mealsRepo;
    private final UserRepository userRepo;
    private List<Meal> meals;

    public SavedMealsPresenter(SavedMealsContracts.View view, MealsRepository mealsRepo, UserRepository userRepo) {
        this.view = view;
        this.mealsRepo = mealsRepo;
        this.userRepo = userRepo;
    }

    @SuppressLint("CheckResult")
    @Override
    public void getSavedMeals() {
        //TODO handle after auth parameter
        mealsRepo.getSavedMeals(userRepo.getCurrentUserId(),false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> {
                            this.meals = meals;
                            view.showSavedMeals(meals);
                        },
                        error -> view.showMessage(error.getMessage())
                );
    }

    @SuppressLint("CheckResult")
    @Override
    public void unSaveMeal(Meal meal) {
        meal.setUserId(userRepo.getCurrentUserId());
        mealsRepo.unSaveMeal(meal).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                () -> {
                    meals.remove(meal);
                    view.showSavedMeals(meals);
                    view.showMessage("Meal unsaved successfully");
                },
                error -> view.showMessage("Can't remove the meal right now, try again later")
        );
    }
}
