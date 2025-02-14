package com.ewida.mealmaster.splash;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ewida.mealmaster.R;
import com.ewida.mealmaster.auth.AuthActivity;
import com.ewida.mealmaster.main.MainActivity;
import com.ewida.mealmaster.onboarding.OnboardingActivity;
import com.ewida.mealmaster.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {

    private static final long SPLASH_SCREEN_DURATION = 3500L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        navigateToFollowingScreen();
    }

    private void navigateToFollowingScreen() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (isFirstOpen()) {
                navigateToOnboarding();
            } else {
                if(isAuthenticated()){
                    navigateToMainScreen();
                }else{
                    navigateToAuthentication();
                }
            }
            finish();
        }, SPLASH_SCREEN_DURATION);
    }

    private boolean isFirstOpen() {
        return getSharedPreferences(Constants.SharedPref.PREFS_FILE_NAME, Context.MODE_PRIVATE)
                .getBoolean(Constants.SharedPref.IS_FIRST_OPEN_KEY, true);
    }

    private boolean isAuthenticated(){
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    private void navigateToOnboarding(){
        startActivity(new Intent(this, OnboardingActivity.class));
    }

    private void navigateToAuthentication(){
        startActivity(new Intent(this, AuthActivity.class));
    }

    private void navigateToMainScreen(){
        startActivity(new Intent(this, MainActivity.class));
    }
}