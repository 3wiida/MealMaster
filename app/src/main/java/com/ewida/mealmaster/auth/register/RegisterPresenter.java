package com.ewida.mealmaster.auth.register;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;

import com.ewida.mealmaster.R;
import com.ewida.mealmaster.model.data_sources.firebase.FirebaseAuthContract;
import com.ewida.mealmaster.model.data_sources.firebase.FirebaseAuthentication;
import com.ewida.mealmaster.utils.Constants;
import com.ewida.mealmaster.utils.enums.FormErrors;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class RegisterPresenter implements RegisterPresenterContract {

    private final RegisterViewContract registerView;
    private final FirebaseAuthContract firebaseAuth;
    private final ObservableArrayList<FormErrors> registerFormErrors;

    public RegisterPresenter(RegisterViewContract registerView) {
        this.registerView = registerView;
        this.firebaseAuth = new FirebaseAuthentication();
        this.registerFormErrors = new ObservableArrayList<>();
    }

    public ObservableArrayList<FormErrors> getFormErrors() {
        return registerFormErrors;
    }

    @Override
    public void handleCreateAccountClick(String email, String password, String confirmPassword) {
        if (isCredentialValid(email, password, confirmPassword)) {
            registerView.showLoaderOnCreateAccountButton();
            registerWithEmailAndPassword(email, password);
        }
    }

    private boolean isCredentialValid(String email, String password, String confirmPassword) {
        registerFormErrors.clear();
        if (!email.matches(Constants.AppRegex.EMAIL_REGEX)) {
            registerFormErrors.add(FormErrors.INVALID_EMAIL);
        }
        if (password.length() < 8) {
            registerFormErrors.add(FormErrors.INVALID_PASSWORD);
        }
        if (!password.equals(confirmPassword)) {
            registerFormErrors.add(FormErrors.INVALID_CONFIRM_PASSWORD);
        }
        return registerFormErrors.isEmpty();
    }

    private void registerWithEmailAndPassword(String email, String password) {
        firebaseAuth.signUpWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
            registerView.navigateToHomeScreen();
        }).addOnFailureListener(error -> registerView.showErrorMessage(error.getMessage()));
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
    public void handleGoogleAuthResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            firebaseAuth.authWithGoogle(account).addOnSuccessListener(authResult -> {
                registerView.navigateToHomeScreen();
            }).addOnFailureListener(error -> registerView.showErrorMessage(error.getMessage()));
        } catch (ApiException exception) {
            registerView.showErrorMessage(exception.getMessage());
        }
    }

}
