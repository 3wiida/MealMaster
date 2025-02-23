package com.ewida.mealmaster.main.home;

import com.ewida.mealmaster.data.model.CategoryMealsResponse;
import com.ewida.mealmaster.data.model.MealResponse;
import com.ewida.mealmaster.data.repository.meals_repo.MealsRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomePresenter implements HomePresenterContract {

    private final HomeViewContract homeView;
    private final MealsRepository repo;

    public HomePresenter(HomeViewContract homeView, MealsRepository repo) {
        this.homeView = homeView;
        this.repo = repo;
    }

    @Override
    public void getRandomMeal() {
        repo.getRandomMeal()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onSuccess(@NonNull MealResponse randomMealResponse) {
                        homeView.showRandomMeal(randomMealResponse.getMeals().get(0));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        homeView.showErrorMessage(e.getMessage());
                    }
                });
    }

    @Override
    public void getVegetarianMeals() {
        repo.getCategoryMeals("Vegetarian")
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
        repo.getCategoryMeals("Dessert")
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

    /*private Meal getRefactoredMeal(MealResponse response) {
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
    }*/
}
