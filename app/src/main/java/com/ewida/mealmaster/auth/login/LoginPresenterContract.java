package com.ewida.mealmaster.auth.login;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public interface LoginPresenterContract {

    void handleLoginClick(Context context, String email, String password);

    void handleGoogleAuthResult(Context context, Task<GoogleSignInAccount> completedTask);

    Intent getGoogleSignInIntent(Context context);


}
