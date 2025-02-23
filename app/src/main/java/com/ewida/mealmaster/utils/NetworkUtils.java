package com.ewida.mealmaster.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.ewida.mealmaster.MealMasterApplication;

public class NetworkUtils {
    public static boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) MealMasterApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }
        return false;
    }
}
