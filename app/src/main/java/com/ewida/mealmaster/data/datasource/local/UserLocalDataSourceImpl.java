package com.ewida.mealmaster.data.datasource.local;

import static com.ewida.mealmaster.utils.Constants.SharedPref.USER_ID_KEY;
import static com.ewida.mealmaster.utils.Constants.SharedPref.USER_NAME_KEY;

import android.content.Context;
import android.content.SharedPreferences;

import com.ewida.mealmaster.data.model.UserStatistics;
import com.ewida.mealmaster.utils.Constants;

import io.reactivex.rxjava3.core.Single;

public class UserLocalDataSourceImpl implements UserLocalDataSource {

    private final SharedPreferences sharedPreferences;
    private static UserLocalDataSource instance;

    private UserLocalDataSourceImpl(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.SharedPref.PREFS_FILE_NAME, Context.MODE_PRIVATE);
    }

    public static UserLocalDataSource getInstance(Context context) {
        if (instance == null) {
            instance = new UserLocalDataSourceImpl(context);
        }
        return instance;
    }

    @Override
    public String getCurrentUserName() {
        return sharedPreferences.getString(USER_NAME_KEY, "Anonymous Guest");
    }

    @Override
    public String getCurrentUserId() {
        return sharedPreferences.getString(USER_ID_KEY, null);
    }

    @Override
    public Boolean isFirstTime() {
        return sharedPreferences.getBoolean(Constants.SharedPref.IS_FIRST_OPEN_KEY, true);
    }

    @Override
    public void setCurrentUserName(String name) {
        sharedPreferences.edit().putString(USER_NAME_KEY, name).apply();
    }

    @Override
    public void setCurrentUserId(String id) {
        sharedPreferences.edit().putString(USER_ID_KEY, id).apply();
    }

    @Override
    public void setFirstTime(boolean isFirstTime) {
        sharedPreferences.edit().putBoolean(Constants.SharedPref.IS_FIRST_OPEN_KEY, isFirstTime).apply();
    }


    @Override
    public void clearUserSession() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(USER_ID_KEY);
        editor.remove(USER_NAME_KEY);
        editor.apply();
    }
}
