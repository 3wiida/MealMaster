package com.ewida.mealmaster.main.home;

import com.ewida.mealmaster.model.pojo.RandomMealResponse;

import io.reactivex.rxjava3.core.Single;

public interface HomePresenterContract {

    void getRandomMeal();
    void getVegetarianMeals();
    void getDesserts();

}
