package com.ewida.mealmaster.main.saved.presenter;

import android.annotation.SuppressLint;
import com.ewida.mealmaster.data.model.Meal;
import com.ewida.mealmaster.data.repository.meals_repo.MealsRepository;
import com.ewida.mealmaster.data.repository.user_repo.UserRepository;
import com.ewida.mealmaster.main.saved.SavedMealsContracts;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SavedMealsPresenter implements SavedMealsContracts.Presenter {

    private final SavedMealsContracts.View view;
    private final MealsRepository mealsRepo;
    private final UserRepository userRepo;

    public SavedMealsPresenter(SavedMealsContracts.View view, MealsRepository mealsRepo, UserRepository userRepo) {
        this.view = view;
        this.mealsRepo = mealsRepo;
        this.userRepo = userRepo;
    }

    @Override
    public boolean isGuest() {
        return userRepo.getCurrentUserId() == null;
    }

    @SuppressLint("CheckResult")
    @Override
    public void getSavedMeals() {
        mealsRepo.getSavedMeals(userRepo.getCurrentUserId(), userRepo.isAfterAuth())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> {
                            view.showSavedMeals(meals);
                            userRepo.setAfterAuth(false);
                            if (meals.isEmpty()) view.showEmptyState();
                        },
                        error -> view.showMessage(error.getMessage())
                );
    }

    @SuppressLint("CheckResult")
    @Override
    public void unSaveMeal(Meal meal) {
        meal.setUserId(userRepo.getCurrentUserId());
        mealsRepo.unSaveMeal(meal).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                () -> view.showUnsavedSnackBar(meal),
                error -> view.showMessage("Can't remove the meal right now, try again later")
        );
    }

    @SuppressLint("CheckResult")
    @Override
    public void saveMeal(Meal meal) {
        mealsRepo.saveMeal(meal).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                () -> {},
                error -> view.showMessage("Can't resave meal")
        );
    }
}
