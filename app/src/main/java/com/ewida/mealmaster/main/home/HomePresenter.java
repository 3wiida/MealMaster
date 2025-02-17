package com.ewida.mealmaster.main.home;

import com.ewida.mealmaster.model.pojo.CategoryMealsResponse;
import com.ewida.mealmaster.model.pojo.Meal;
import com.ewida.mealmaster.model.pojo.RandomMealResponse;
import com.ewida.mealmaster.network.ApiClient;

import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomePresenter implements HomePresenterContract {

    private final HomeViewContract homeView;
    private final String VEGETARIAN_CATEGORY_FILTER = "Vegetarian";
    private final String DESSERTS_CATEGORY_FILTER = "Dessert";

    public HomePresenter(HomeViewContract homeView) {
        this.homeView = homeView;
    }

    @Override
    public void getRandomMeal() {
        ApiClient.getInstance().getRandomMeal()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onSuccess(@NonNull RandomMealResponse randomMealResponse) {
                        homeView.showRandomMeal(getRefactoredMeal(randomMealResponse));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        homeView.showErrorMessage(e.getMessage());
                    }
                });
    }

    @Override
    public void getVegetarianMeals() {
        ApiClient.getInstance().getCategoryMeals(VEGETARIAN_CATEGORY_FILTER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {}

                    @Override
                    public void onSuccess(@NonNull CategoryMealsResponse categoryMealsResponse) {
                        homeView.showVegetarianMeals(categoryMealsResponse.getMeals());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        homeView.showErrorMessage(e.getMessage());
                    }
                });
    }

    @Override
    public void getDesserts() {
        ApiClient.getInstance().getCategoryMeals(DESSERTS_CATEGORY_FILTER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {}

                    @Override
                    public void onSuccess(@NonNull CategoryMealsResponse categoryMealsResponse) {
                        homeView.showDesserts(categoryMealsResponse.getMeals());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        homeView.showErrorMessage(e.getMessage());
                    }
                });
    }

    private Meal getRefactoredMeal(RandomMealResponse response) {
        Meal meal = response.getMeals().get(0);
        if (meal.getStrTags() == null || meal.getStrTags().trim().equals(" ")) {
            meal.setStrTags("N/A");
        } else {
            List<String> tags = Arrays.asList(meal.getStrTags().split(","));
            StringBuilder refactoredTags = new StringBuilder();
            for (int i = 0; i < tags.size(); i++) {
                refactoredTags.append(tags.get(i));
                if (i != tags.size() - 1) {
                    refactoredTags.append(", ");
                }
            }
            meal.setStrTags(refactoredTags.toString());
        }
        return meal;
    }
}
