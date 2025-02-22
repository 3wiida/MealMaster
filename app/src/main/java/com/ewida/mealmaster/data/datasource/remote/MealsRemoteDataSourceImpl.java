package com.ewida.mealmaster.data.datasource.remote;

import android.util.Log;

import com.ewida.mealmaster.data.datasource.remote.api.ApiServices;
import com.ewida.mealmaster.data.model.AreasResponse;
import com.ewida.mealmaster.data.model.CategoriesResponse;
import com.ewida.mealmaster.data.model.CategoryMealsResponse;
import com.ewida.mealmaster.data.model.IngredientsResponse;
import com.ewida.mealmaster.data.model.MealResponse;
import io.reactivex.rxjava3.core.Single;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MealsRemoteDataSourceImpl implements MealsRemoteDataSource {

    private final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private final String TAG = "ApiClient";
    private final ApiServices apiServices;
    private static MealsRemoteDataSource instance;

    private MealsRemoteDataSourceImpl(){
        Log.d(TAG, "MealsRemoteDataSourceImpl: ");
        apiServices = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
                .create(ApiServices.class);
    }

    public static MealsRemoteDataSource getInstance(){
        if(instance == null){
            instance = new MealsRemoteDataSourceImpl();
        }
        return instance;
    }

    private OkHttpClient getHttpClient(){
        HttpLoggingInterceptor httpClientLoggingInterceptor=new HttpLoggingInterceptor(log -> {
            Log.d(TAG, "getHttpClient: " + log);
        });
        httpClientLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder().addInterceptor(httpClientLoggingInterceptor).build();
    }


    @Override
    public Single<MealResponse> getRandomMeal() {
        return apiServices.getRandomMeal();
    }

    @Override
    public Single<CategoryMealsResponse> getCategoryMeals(String category) {
        return apiServices.getCategoryMeals(category);
    }

    @Override
    public Single<CategoryMealsResponse> getAreaMeals(String area) {
        return apiServices.getAreaMeals(area);
    }

    @Override
    public Single<CategoryMealsResponse> getIngredientMeals(String ingredient) {
        return apiServices.getIngredientMeals(ingredient);
    }

    @Override
    public Single<MealResponse> getMealById(String id) {
        return apiServices.getMealById(id);
    }

    @Override
    public Single<MealResponse> search(String query) {
        return apiServices.search(query);
    }

    @Override
    public Single<CategoriesResponse> getAllCategories() {
        return apiServices.getAllCategories();
    }

    @Override
    public Single<AreasResponse> getAllAreas() {
        return apiServices.getAllAreas("list");
    }

    @Override
    public Single<IngredientsResponse> getAllIngredients() {
        return apiServices.getAllIngredients("list");
    }
}
