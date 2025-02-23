package com.ewida.mealmaster.auth.login;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

public interface LoginContracts {
    interface Presenter{
        void handleLoginClick(String email, String password);

        void handleGoogleAuthResult(Task<GoogleSignInAccount> completedTask);

        Intent getGoogleSignInIntent(Context context);
    }

    interface View{
        void navigateToHomeScreen();
        void showLoaderOnLoginButton();
        void showErrorMessage(String msg);
    }
}
