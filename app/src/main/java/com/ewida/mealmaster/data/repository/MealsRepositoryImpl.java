package com.ewida.mealmaster.data.repository;

import com.ewida.mealmaster.data.datasource.remote.MealsRemoteDataSource;
import com.ewida.mealmaster.data.model.CategoryMealsResponse;
import com.ewida.mealmaster.data.model.MealResponse;
import io.reactivex.rxjava3.core.Single;

public class MealsRepositoryImpl implements MealsRepository{

    private final MealsRemoteDataSource remoteDataSource;
    private static MealsRepository instance;

    private MealsRepositoryImpl(MealsRemoteDataSource remoteDataSource) {
        this.remoteDataSource = remoteDataSource;
    }

    public static synchronized MealsRepository getInstance(MealsRemoteDataSource remote){
        if(instance == null){
            instance = new MealsRepositoryImpl(remote);
        }
        return instance;
    }

    @Override
    public Single<MealResponse> getRandomMeal() {
        return remoteDataSource.getRandomMeal();
    }

    @Override
    public Single<CategoryMealsResponse> getCategoryMeals(String category) {
        return remoteDataSource.getCategoryMeals(category);
    }

    @Override
    public Single<MealResponse> getMealById(String id) {
        return remoteDataSource.getMealById(id);
    }
}
