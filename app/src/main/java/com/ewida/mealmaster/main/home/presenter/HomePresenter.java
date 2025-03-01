package com.ewida.mealmaster.main.home.presenter;

import android.annotation.SuppressLint;

import com.ewida.mealmaster.data.repository.meals_repo.MealsRepository;
import com.ewida.mealmaster.data.repository.user_repo.UserRepository;
import com.ewida.mealmaster.main.home.HomeContracts;


import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomePresenter implements HomeContracts.Presenter {

    private final HomeContracts.View view;
    private final MealsRepository mealsRepo;
    private final UserRepository userRepo;

    public HomePresenter(HomeContracts.View view, MealsRepository repo, UserRepository userRepo) {
        this.view = view;
        this.mealsRepo = repo;
        this.userRepo = userRepo;
    }

    @Override
    public void getUsername() {
        String name = userRepo.getCurrentUserName();
        view.showUsername(name != null ? name : "Anonymous Guest");
    }

    @SuppressLint("CheckResult")
    @Override
    public void getRandomMeal() {
        mealsRepo.getRandomMeal()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> view.showRandomMeal(response.getMeals().get(0)),
                        error-> view.showErrorMessage(error.getMessage())
                );
    }

    @SuppressLint("CheckResult")
    @Override
    public void getVegetarianMeals() {
        mealsRepo.getCategoryMeals("Vegetarian")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> view.showVegetarianMeals(response.getMeals()),
                        error-> view.showErrorMessage(error.getMessage())
                );
    }

    @SuppressLint("CheckResult")
    @Override
    public void getDesserts() {
        mealsRepo.getCategoryMeals("Dessert")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> view.showDesserts(response.getMeals()),
                        error-> view.showErrorMessage(error.getMessage())
                );
    }

}
