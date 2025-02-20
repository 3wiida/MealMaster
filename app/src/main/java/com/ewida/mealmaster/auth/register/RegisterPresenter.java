package com.ewida.mealmaster.auth.register;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.ObservableArrayList;
import com.ewida.mealmaster.R;
import com.ewida.mealmaster.model.data_sources.firebase.auth.FirebaseAuthContract;
import com.ewida.mealmaster.model.data_sources.firebase.auth.FirebaseAuthentication;
import com.ewida.mealmaster.model.data_sources.firebase.database.FirebaseDB;
import com.ewida.mealmaster.model.data_sources.firebase.database.FirebaseDatabaseContract;
import com.ewida.mealmaster.data.model.User;
import com.ewida.mealmaster.utils.Constants;
import com.ewida.mealmaster.utils.enums.FormErrors;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class RegisterPresenter implements RegisterPresenterContract {

    private final RegisterViewContract registerView;
    private final FirebaseAuthContract firebaseAuth;
    private final FirebaseDatabaseContract firebaseDB;
    private final ObservableArrayList<FormErrors> registerFormErrors;

    public RegisterPresenter(RegisterViewContract registerView) {
        this.registerView = registerView;
        this.firebaseAuth = new FirebaseAuthentication();
        this.firebaseDB = new FirebaseDB();
        this.registerFormErrors = new ObservableArrayList<>();
    }

    public ObservableArrayList<FormErrors> getFormErrors() {
        return registerFormErrors;
    }

    @Override
    public void handleCreateAccountClick(Context context, String fullName, String email, String password) {
        if (isCredentialValid(fullName, email, password)) {
            registerView.showLoaderOnCreateAccountButton();
            registerWithEmailAndPassword(context, fullName, email, password);
        }
    }

    private boolean isCredentialValid(String fullName, String email, String password) {
        registerFormErrors.clear();
        if (!email.matches(Constants.AppRegex.EMAIL_REGEX)) {
            registerFormErrors.add(FormErrors.INVALID_EMAIL);
        }
        if (password.length() < 8) {
            registerFormErrors.add(FormErrors.INVALID_PASSWORD);
        }
        if (fullName.length() < 3) {
            registerFormErrors.add(FormErrors.INVALID_NAME);
        }
        return registerFormErrors.isEmpty();
    }

    private void registerWithEmailAndPassword(Context context, String fullName, String email, String password) {
        firebaseAuth.signUpWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
            User user = new User(authResult.getUser().getUid(), fullName);
            insertUserInDatabase(context, user);
        }).addOnFailureListener(error -> registerView.showErrorMessage(error.getMessage()));
    }

    private void insertUserInDatabase(Context context, User user) {
        firebaseDB.insertUser(user).addOnSuccessListener(unused -> {
            saveUserNameInSharedPref(context, user.getName());
            registerView.navigateToHomeScreen();
        }).addOnFailureListener(error -> {
            registerView.showErrorMessage(error.getMessage());
        });
    }

    private void saveUserNameInSharedPref(Context context, String username) {
        context.getSharedPreferences(Constants.SharedPref.PREFS_FILE_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString(Constants.SharedPref.USER_NAME_KEY, username)
                .apply();
    }

    @Override
    public Intent getGoogleAuthIntent(Context context) {
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
                registerView.navigateToHomeScreen();
            }).addOnFailureListener(error -> registerView.showErrorMessage(error.getMessage()));
        } catch (ApiException exception) {
            registerView.showErrorMessage(exception.getMessage());
        }
    }

}
