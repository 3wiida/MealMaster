package com.ewida.mealmaster.data.repository.meals_repo;

import android.annotation.SuppressLint;

import com.ewida.mealmaster.data.datasource.local.MealsLocalDataSource;
import com.ewida.mealmaster.data.datasource.remote.MealsRemoteDataSource;
import com.ewida.mealmaster.data.model.AreasResponse;
import com.ewida.mealmaster.data.model.CategoriesResponse;
import com.ewida.mealmaster.data.model.CategoryMealsResponse;
import com.ewida.mealmaster.data.model.IngredientsResponse;
import com.ewida.mealmaster.data.model.Meal;
import com.ewida.mealmaster.data.model.MealResponse;
import com.ewida.mealmaster.utils.NetworkUtils;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class MealsRepositoryImpl implements MealsRepository {

    private final MealsRemoteDataSource remoteDataSource;
    private final MealsLocalDataSource localDataSource;
    private static MealsRepository instance;

    public MealsRepositoryImpl(MealsRemoteDataSource remoteDataSource, MealsLocalDataSource localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    public static synchronized MealsRepository getInstance(MealsRemoteDataSource remote, MealsLocalDataSource local) {
        if (instance == null) {
            instance = new MealsRepositoryImpl(remote, local);
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
    public Single<CategoryMealsResponse> getAreaMeals(String area) {
        return remoteDataSource.getAreaMeals(area);
    }

    @Override
    public Single<CategoryMealsResponse> getIngredientMeals(String ingredient) {
        return remoteDataSource.getIngredientMeals(ingredient);
    }

    @Override
    public Single<MealResponse> getMealById(String id) {
        return remoteDataSource.getMealById(id);
    }

    @Override
    public Single<MealResponse> search(String query) {
        return remoteDataSource.search(query);
    }

    @Override
    public Single<CategoriesResponse> getAllCategories() {
        return remoteDataSource.getAllCategories();
    }

    @Override
    public Single<AreasResponse> getAllAreas() {
        return remoteDataSource.getAllAreas();
    }

    @Override
    public Single<IngredientsResponse> getAllIngredients() {
        return remoteDataSource.getAllIngredients();
    }

    @Override
    public Completable saveMeal(Meal meal) {
        return localDataSource.saveMeal(meal);
    }

    @Override
    public Completable unSaveMeal(Meal meal) {
        return localDataSource.unSaveMeal(meal);
    }

    @Override
    public Single<Boolean> isMealSaved(String mealId, String userId) {
        return localDataSource.isMealSaved(mealId, userId);
    }

    @SuppressLint("CheckResult")
    @Override
    public Single<List<Meal>> getSavedMeals(String userId, boolean isAfterAuth) {
        return Single.create(emitter -> {
            if (!NetworkUtils.isNetworkAvailable()) {
                localDataSource.getSavedMeals(userId).subscribe(emitter::onSuccess, emitter::onError);
            } else {
                if(isAfterAuth){
                    remoteDataSource.getSavedMeals(userId).get().addOnSuccessListener(dataSnapshot -> {
                        List<Meal> meals = new ArrayList<>();
                        for (DataSnapshot mealSnapshot : dataSnapshot.getChildren()) {
                            Meal meal = mealSnapshot.getValue(Meal.class);
                            if (meal != null) {
                                meals.add(meal);
                            }
                        }

                        localDataSource.saveMeals(meals).subscribe(
                                () -> localDataSource.getSavedMeals(userId).subscribe(emitter::onSuccess, emitter::onError),
                                emitter::onError
                        );
                    }).addOnFailureListener(emitter::onError);
                }else{
                    localDataSource.getSavedMeals(userId).subscribe(emitter::onSuccess, emitter::onError);
                }
            }
        });
    }
}
