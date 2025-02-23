package com.ewida.mealmaster.meal_details.presenter;

import android.annotation.SuppressLint;

import com.ewida.mealmaster.data.model.Meal;

import com.ewida.mealmaster.data.repository.meals_repo.MealsRepository;
import com.ewida.mealmaster.data.repository.user_repo.UserRepository;
import com.ewida.mealmaster.meal_details.MealDetailsContracts;
import com.google.firebase.auth.FirebaseAuth;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealDetailsPresenter implements MealDetailsContracts.Presenter {

    private final MealDetailsContracts.View view;
    private final MealsRepository mealsRepo;
    private final UserRepository userRepo;

    public MealDetailsPresenter(
            MealDetailsContracts.View view,
            MealsRepository mealsRepo,
            UserRepository userRepo
    ) {
        this.view = view;
        this.mealsRepo = mealsRepo;
        this.userRepo = userRepo;
    }

    @SuppressLint("CheckResult")
    @Override
    public void getMealById(String id) {
        mealsRepo.getMealById(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                mealResponse -> view.showMealDetails(mealResponse.getMeals().get(0)),
                error -> view.showMessage(error.getMessage())
        );
    }

    @SuppressLint("CheckResult")
    @Override
    public void saveMeal(Meal meal) {
        meal.setUserId(userRepo.getCurrentUserId());
        mealsRepo.saveMeal(meal).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                () -> view.showMessage("Meal saved successfully"),
                error -> view.showMessage("Can't save meal right now, try again later")
        );
    }

    @SuppressLint("CheckResult")
    @Override
    public void unSaveMeal(Meal meal) {
        meal.setUserId(userRepo.getCurrentUserId());
        mealsRepo.unSaveMeal(meal).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                () -> view.showMessage("Meal unsaved successfully"),
                error -> view.showMessage("Can't remove the meal right now, try again later")
        );
    }

    @SuppressLint("CheckResult")
    @Override
    public void isMealSaved(String mealId) {
        mealsRepo.isMealSaved(
                mealId,
                userRepo.getCurrentUserId()
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(view::setSavedIcon);
    }
}
