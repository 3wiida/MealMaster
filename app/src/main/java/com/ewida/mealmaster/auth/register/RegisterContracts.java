package com.ewida.mealmaster.auth.register;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

public interface RegisterContracts {
    interface Presenter{
        void handleCreateAccountClick(String email, String password, String confirmPassword);
        void handleGoogleAuthResult(Task<GoogleSignInAccount> completedTask);
        Intent getGoogleAuthIntent(Context context);
    }

    interface View{
        void navigateToHomeScreen();
        void showLoaderOnCreateAccountButton();
        void showErrorMessage(String msg);
    }
}
