package com.ewida.mealmaster.main.plans.presenter;

import android.annotation.SuppressLint;

import com.ewida.mealmaster.data.model.Meal;
import com.ewida.mealmaster.data.model.Plan;
import com.ewida.mealmaster.data.repository.meals_repo.MealsRepository;
import com.ewida.mealmaster.data.repository.user_repo.UserRepository;
import com.ewida.mealmaster.main.plans.PlannedMealsContracts;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PlannedMealsPresenter implements PlannedMealsContracts.presenter {
    private final PlannedMealsContracts.View view;
    private final MealsRepository mealsRepo;
    private final UserRepository userRepo;
    private Disposable mealsFlowable;

    public PlannedMealsPresenter(PlannedMealsContracts.View view, MealsRepository mealsRepo, UserRepository userRepo) {
        this.view = view;
        this.mealsRepo = mealsRepo;
        this.userRepo = userRepo;
    }

    @SuppressLint("CheckResult")
    @Override
    public void getPlannedMeals(String date) {
        if (mealsFlowable != null) {
            mealsFlowable.dispose();
        }
        mealsFlowable = mealsRepo.getPlanedMeals(userRepo.getCurrentUserId(), date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        plans -> {
                            List<Meal> meals = new ArrayList<>();
                            plans.forEach(plan -> meals.add(plan.getMeal()));
                            view.showPlannedMeals(meals);
                        },
                        error -> view.showMessage(error.getMessage())
                );
    }

    @SuppressLint("CheckResult")
    @Override
    public void unPlanMeal(Meal meal, String date) {
        Plan plan = new Plan(date, meal);
        mealsRepo.unPlanMeal(plan).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                () -> view.showRemovedSnackBar(plan),
                error -> view.showMessage(error.getMessage())
        );
    }

    @SuppressLint("CheckResult")
    @Override
    public void planMeal(Plan plan) {
        mealsRepo.planMeal(plan).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                () -> {
                },
                error -> view.showMessage(error.getMessage())
        );
    }
}
