package com.ewida.mealmaster.data.datasource.local;

import android.content.Context;

import com.ewida.mealmaster.data.datasource.local.db.MealsDao;
import com.ewida.mealmaster.data.datasource.local.db.MealsDatabase;
import com.ewida.mealmaster.data.model.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

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
    public Single<Boolean> isMealSaved(String mealId, String userId) {
        return dao.isMealSaved(mealId, userId);
    }

    @Override
    public Single<List<Meal>> getSavedMeals(String userId) {
        return dao.getSavedMeals(userId);
    }

    @Override
    public Completable saveMeals(List<Meal> meals) {
        return Completable.fromAction(() -> dao.insertMeals(meals))
                .subscribeOn(Schedulers.io());
    }
}
