package com.ewida.mealmaster.meal_details.presenter;

import android.annotation.SuppressLint;
import com.ewida.mealmaster.data.repository.MealsRepository;
import com.ewida.mealmaster.meal_details.MealDetailsContracts;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealDetailsPresenter implements MealDetailsContracts.Presenter {

    private final MealDetailsContracts.View view;
    private final MealsRepository repo;

    public MealDetailsPresenter(MealDetailsContracts.View view, MealsRepository repo) {
        this.view = view;
        this.repo = repo;
    }

    @SuppressLint("CheckResult")
    @Override
    public void getMealById(String id) {
        repo.getMealById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mealResponse -> view.showMealDetails(mealResponse.getMeals().get(0)),
                        error -> view.showErrorMessage(error.getMessage())
                );
    }
}
