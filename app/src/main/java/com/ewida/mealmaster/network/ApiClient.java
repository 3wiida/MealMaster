package com.ewida.mealmaster.network;

import android.util.Log;

import androidx.annotation.NonNull;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static ApiServices apiServices = null;
    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private static final String TAG = "ApiClient";

    private ApiClient(){}

    private static OkHttpClient getHttpClient(){
        HttpLoggingInterceptor httpClientLoggingInterceptor=new HttpLoggingInterceptor(log -> {
            Log.d(TAG, "getHttpClient: " + log);
        });
        httpClientLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder().addInterceptor(httpClientLoggingInterceptor).build();
    }

    public static synchronized ApiServices getInstance(){
        if(apiServices == null){
            apiServices = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(getHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build()
                    .create(ApiServices.class);
        }
        return apiServices;
    }

}
