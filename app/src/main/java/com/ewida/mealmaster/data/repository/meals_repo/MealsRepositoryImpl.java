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
import com.ewida.mealmaster.data.model.Plan;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

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
    public Single<Boolean> isMealSaved(String mealId) {
        return localDataSource.isMealSaved(mealId);
    }

    @SuppressLint("CheckResult")
    @Override
    public Flowable<List<Meal>> getSavedMeals() {
        /*return Flowable.create(emitter -> {
            if (!NetworkUtils.isNetworkAvailable()) {
                Log.d("```TAG```", "getSavedMeals: no network");
                localDataSource.getSavedMeals(userId).subscribe(
                        emitter::onNext,
                        emitter::onError
                );
            } else {
                Log.d("```TAG```", "getSavedMeals: there is network");
                if (isAfterAuth) {
                    Log.d("```TAG```", "getSavedMeals: after auth");
                    remoteDataSource.getSavedMeals(userId).get().addOnSuccessListener(dataSnapshot -> {
                        List<Meal> meals = new ArrayList<>();
                        for (DataSnapshot mealSnapshot : dataSnapshot.getChildren()) {
                            Meal meal = mealSnapshot.getValue(Meal.class);
                            if (meal != null) {
                                meals.add(meal);
                            }
                        }

                        localDataSource.saveMeals(meals).subscribe(
                                () -> localDataSource.getSavedMeals(userId).subscribe(emitter::onNext, emitter::onError),
                                emitter::onError
                        );
                    }).addOnFailureListener(emitter::onError);
                } else {
                    Log.d("```TAG```", "getSavedMeals: not after auth");
                    localDataSource.getSavedMeals(userId).subscribe(emitter::onNext, emitter::onError);
                }
            }
        }, BackpressureStrategy.DROP);*/
        return localDataSource.getSavedMeals();
    }

    @SuppressLint("CheckResult")
    @Override
    public Completable syncUserData(String userId) {
        return Completable.create(emitter -> {
            remoteDataSource.getSavedMeals(userId).get().addOnSuccessListener(mealsSnapshot -> {
                List<Meal> savedMeals = buildSavedMealsList(mealsSnapshot);
                localDataSource.saveMeals(savedMeals).subscribeOn(Schedulers.io()).subscribe(
                        ()->{
                            remoteDataSource.getPlannedMeals(userId).get().addOnSuccessListener(plansSnapshot -> {
                                List<Plan> plans = buildPlansList(plansSnapshot);
                                localDataSource.planMeals(plans).subscribeOn(Schedulers.io()).subscribe(
                                        emitter::onComplete,
                                        emitter::onError
                                );
                            }).addOnFailureListener(emitter::onError);
                        },
                        emitter::onError
                );
            }).addOnFailureListener(emitter::onError);
        });
    }

    private List<Meal> buildSavedMealsList(DataSnapshot dataSnapshot){
        List<Meal> meals = new ArrayList<>();
        for (DataSnapshot mealSnapshot : dataSnapshot.getChildren()) {
            Meal meal = mealSnapshot.getValue(Meal.class);
            if (meal != null) {
                meals.add(meal);
            }
        }
        return meals;
    }

    private List<Plan> buildPlansList(DataSnapshot dataSnapshot){
        List<Plan> plans = new ArrayList<>();
        for (DataSnapshot mealSnapshot : dataSnapshot.getChildren()) {
            Plan plan = mealSnapshot.getValue(Plan.class);
            if (plan != null) {
                plans.add(plan);
            }
        }
        return plans;
    }

    @Override
    public Completable planMeal(Plan plan) {
        return localDataSource.planMeal(plan);
    }

    @Override
    public Completable unPlanMeal(Plan plan) {
        return localDataSource.unPlanMeal(plan);
    }

    @Override
    public Flowable<List<Plan>> getPlanedMeals(String date) {
        return localDataSource.getPlanedMeals(date);
    }

    @Override
    public Single<List<Plan>> getAllPlans() {
        return localDataSource.getAllPlans();
    }
}
