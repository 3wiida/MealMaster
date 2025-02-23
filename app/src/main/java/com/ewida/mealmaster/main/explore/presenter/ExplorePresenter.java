package com.ewida.mealmaster.main.explore.presenter;

import android.annotation.SuppressLint;

import com.ewida.mealmaster.data.model.Area;
import com.ewida.mealmaster.data.model.Category;
import com.ewida.mealmaster.data.model.ExploreItem;
import com.ewida.mealmaster.data.model.IngredientApiModel;
import com.ewida.mealmaster.data.repository.meals_repo.MealsRepository;
import com.ewida.mealmaster.explore_meals.ExploreMealsContracts;
import com.ewida.mealmaster.main.explore.ExploreContracts;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ExplorePresenter implements ExploreContracts.Presenter {

    private final ExploreContracts.View view;
    private final MealsRepository repo;
    private List<ExploreItem> categories;
    private List<ExploreItem> areas;
    private List<ExploreItem> ingredients;

    public ExplorePresenter(ExploreContracts.View view, MealsRepository repo) {
        this.view = view;
        this.repo = repo;
    }

    @SuppressLint("CheckResult")
    @Override
    public void getAllCategories() {
        repo.getAllCategories().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                response -> {
                    view.hideCategoriesLoadingState();
                    categories = response.getCategories().stream().map(Category::toExploreItem).collect(Collectors.toList());
                    view.showAllCategories(categories);
                },
                error -> view.showErrorMessage(error.getMessage())
        );
    }

    @SuppressLint("CheckResult")
    @Override
    public void getAllAreas() {
        repo.getAllAreas().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                response -> {
                    view.hideAreasLoadingState();
                    areas = response.getAreas().stream().map(Area::toExploreItem).collect(Collectors.toList());
                    view.showAllAreas(areas);
                },
                error -> view.showErrorMessage(error.getMessage())
        );
    }

    @SuppressLint("CheckResult")
    @Override
    public void getAllIngredients() {
        repo.getAllIngredients().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                response -> {
                    view.hideIngredientsLoadingState();
                    ingredients = response.getIngredients().stream().map(IngredientApiModel::toExploreItem).collect(Collectors.toList());
                    view.showAllIngredients(ingredients);
                },
                error -> view.showErrorMessage(error.getMessage())
        );
    }

    @Override
    public void search(String query, ExploreMealsContracts.ExploreType type) {
        List<ExploreItem> results;
        if(query.trim().isEmpty()){
            view.hideSearchResults();
            view.showSectionGroup();
        }else{
            view.hideSectionGroup();
            switch (type){
                case CATEGORY:
                    results = categories.stream().filter(category-> category.getTitle().contains(query)).collect(Collectors.toList());
                    break;
                case AREA:
                    results = areas.stream().filter(category-> category.getTitle().contains(query)).collect(Collectors.toList());
                    break;
                case INGREDIENT:
                    results = ingredients.stream().filter(category-> category.getTitle().contains(query)).collect(Collectors.toList());
                    break;
                default:
                    results = new ArrayList<>();
            }
            view.showSearchResults(results);
        }
    }
}
