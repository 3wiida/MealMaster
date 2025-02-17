package com.ewida.mealmaster.auth.login;


import android.content.Context;
import android.content.Intent;

import androidx.databinding.ObservableArrayList;

import com.ewida.mealmaster.R;
import com.ewida.mealmaster.model.data_sources.firebase.auth.FirebaseAuthContract;
import com.ewida.mealmaster.model.data_sources.firebase.auth.FirebaseAuthentication;
import com.ewida.mealmaster.model.data_sources.firebase.database.FirebaseDB;
import com.ewida.mealmaster.model.data_sources.firebase.database.FirebaseDatabaseContract;
import com.ewida.mealmaster.model.pojo.User;
import com.ewida.mealmaster.utils.Constants;
import com.ewida.mealmaster.utils.enums.FormErrors;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginPresenter implements LoginPresenterContract {
    private final LoginViewContract loginView;
    private final FirebaseAuthContract firebaseAuth;
    private final FirebaseDatabaseContract firebaseDB;
    private final ObservableArrayList<FormErrors> loginFormErrors;

    public LoginPresenter(LoginViewContract loginView) {
        this.loginView = loginView;
        this.firebaseAuth = new FirebaseAuthentication();
        this.firebaseDB = new FirebaseDB();
        this.loginFormErrors = new ObservableArrayList<>();
    }

    public ObservableArrayList<FormErrors> getFormErrors() {
        return this.loginFormErrors;
    }

    @Override
    public void handleLoginClick(Context context, String email, String password) {
        if (isCredentialValid(email, password)) {
            loginView.showLoaderOnLoginButton();
            loginWithEmailAndPassword(context, email, password);
        }
    }

    private boolean isCredentialValid(String email, String password) {
        loginFormErrors.clear();
        if (!email.matches(Constants.AppRegex.EMAIL_REGEX)) {
            loginFormErrors.add(FormErrors.INVALID_EMAIL);
        }
        if (password.length() < 8) {
            loginFormErrors.add(FormErrors.INVALID_PASSWORD);
        }
        return loginFormErrors.isEmpty();
    }

    private void loginWithEmailAndPassword(Context context, String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
            getUsernameFromDatabase(context, authResult.getUser().getUid());
        }).addOnFailureListener(error -> {
            loginView.showErrorMessage(error.getMessage());
        });
    }

    private void getUsernameFromDatabase(Context context, String id) {
        firebaseDB.getUserByID(id).get().addOnSuccessListener(dataSnapshot -> {
            String username = dataSnapshot.getValue(User.class).getName();
            saveUserNameInSharedPref(context, username);
            loginView.navigateToHomeScreen();
        }).addOnFailureListener(error -> {
            loginView.showErrorMessage(error.getMessage());
        });
    }

    private void saveUserNameInSharedPref(Context context, String username) {
        context.getSharedPreferences(Constants.SharedPref.PREFS_FILE_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString(Constants.SharedPref.USER_NAME_KEY, username)
                .apply();
    }

    @Override
    public Intent getGoogleSignInIntent(Context context) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        return GoogleSignIn.getClient(context, gso).getSignInIntent();
    }

    @Override
    public void handleGoogleAuthResult(Context context, Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            firebaseAuth.authWithGoogle(account).addOnSuccessListener(authResult -> {
                saveUserNameInSharedPref(context,authResult.getUser().getDisplayName());
                loginView.navigateToHomeScreen();
            }).addOnFailureListener(error -> {
                loginView.showErrorMessage(error.getMessage());
            });
        } catch (ApiException e) {
            loginView.showErrorMessage(e.getMessage());
        }
    }
}
