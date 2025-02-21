package com.ewida.mealmaster.main.explore.presenter;

import android.annotation.SuppressLint;

import com.ewida.mealmaster.data.model.Area;
import com.ewida.mealmaster.data.model.Category;
import com.ewida.mealmaster.data.model.IngredientApiModel;
import com.ewida.mealmaster.data.repository.MealsRepository;
import com.ewida.mealmaster.main.explore.ExploreContracts;
import java.util.stream.Collectors;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ExplorePresenter implements ExploreContracts.Presenter {

    private ExploreContracts.View view;
    private MealsRepository repo;

    public ExplorePresenter(ExploreContracts.View view, MealsRepository repo) {
        this.view = view;
        this.repo = repo;
    }

    @SuppressLint("CheckResult")
    @Override
    public void getAllCategories() {
        repo.getAllCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            view.showAllCategories(
                                    response.getCategories().stream().map(Category::toExploreItem).collect(Collectors.toList())
                            );
                        },
                        error -> {
                            view.showErrorMessage(error.getMessage());
                        }
                );
    }

    @SuppressLint("CheckResult")
    @Override
    public void getAllAreas() {
        repo.getAllAreas()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            view.showAllAreas(
                                    response.getAreas().stream().map(Area::toExploreItem).collect(Collectors.toList())
                            );
                        },
                        error -> {
                            view.showErrorMessage(error.getMessage());
                        }
                );
    }

    @SuppressLint("CheckResult")
    @Override
    public void getAllIngredients() {
        repo.getAllIngredients()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            view.showAllIngredients(
                                    response.getIngredients().stream().map(IngredientApiModel::toExploreItem).collect(Collectors.toList())
                            );
                        },
                        error -> {
                            view.showErrorMessage(error.getMessage());
                        }
                );
    }
}
