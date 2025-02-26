package com.ewida.mealmaster.data.datasource.local;

import android.content.Context;

import com.ewida.mealmaster.data.datasource.local.db.MealsDao;
import com.ewida.mealmaster.data.datasource.local.db.MealsDatabase;
import com.ewida.mealmaster.data.model.Meal;
import com.ewida.mealmaster.data.model.Plan;
import com.ewida.mealmaster.data.model.UserStatistics;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class MealsLocalDataSourceImpl implements MealsLocalDataSource{
    private final MealsDao dao;
    private static MealsLocalDataSource instance = null;

    public MealsLocalDataSourceImpl(Context context) {
        this.dao = MealsDatabase.getInstance(context).getDao();
    }

    public static MealsLocalDataSource getInstance(Context context) {
        if (instance == null) {
            instance = new MealsLocalDataSourceImpl(context);
        }
        return instance;
    }

    @Override
    public Completable saveMeal(Meal meal) {
        return dao.saveMeal(meal);
    }

    @Override
    public Completable unSaveMeal(Meal meal) {
        return dao.unSaveMeal(meal);
    }

    @Override
    public Single<Boolean> isMealSaved(String mealId) {
        return dao.isMealSaved(mealId);
    }

    @Override
    public Flowable<List<Meal>> getSavedMeals() {
        return dao.getSavedMeals();
    }

    @Override
    public Completable saveMeals(List<Meal> meals) {
        return dao.insertMeals(meals);
    }

    @Override
    public Completable planMeal(Plan plan) {
        return dao.planMeal(plan);
    }

    @Override
    public Completable planMeals(List<Plan> plans) {
        return dao.planMeals(plans);
    }

    @Override
    public Completable unPlanMeal(Plan plan) {
        return dao.unPlanMeal(plan);
    }

    @Override
    public Flowable<List<Plan>> getPlanedMeals(String date) {
        return dao.getPlanedMeals(date);
    }

    @Override
    public Single<List<Plan>> getAllPlans() {
        return dao.getAllPlans();
    }

    @Override
    public Single<UserStatistics> getUserStatistics() {
        return dao.getUerStatistics();
    }

    @Override
    public Completable clearSavedMeals() {
        return dao.clearSavedMeals();
    }

    @Override
    public Completable clearPlans() {
        return dao.clearPlans();
    }
}
