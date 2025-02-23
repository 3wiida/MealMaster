package com.ewida.mealmaster.explore_meals.presenter;

import android.annotation.SuppressLint;

import com.ewida.mealmaster.data.model.CategoryMealsResponse;
import com.ewida.mealmaster.data.repository.meals_repo.MealsRepository;
import com.ewida.mealmaster.explore_meals.ExploreMealsContracts;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ExploreMealsPresenter implements ExploreMealsContracts.Presenter {

    private final ExploreMealsContracts.View view;
    private final MealsRepository repo;

    public ExploreMealsPresenter(ExploreMealsContracts.View view, MealsRepository repo) {
        this.view = view;
        this.repo = repo;
    }

    @SuppressLint("CheckResult")
    @Override
    public void getMeals(ExploreMealsContracts.ExploreType type, String query) {
        Single<CategoryMealsResponse> requestObservable = null;
        switch (type){
            case CATEGORY:
                requestObservable = repo.getCategoryMeals(query);
                break;
            case AREA:
                requestObservable = repo.getAreaMeals(query);
                break;
            case INGREDIENT:
                requestObservable = repo.getIngredientMeals(query);
                break;
        }
        requestObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                response->{
                    view.hideLoadingState();
                    view.showMealsList(response.getMeals());
                },
                error-> view.showErrorMsg(error.getMessage())
        );
    }
}
