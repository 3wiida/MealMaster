package com.ewida.mealmaster.auth.login.presenter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import androidx.databinding.ObservableArrayList;

import com.ewida.mealmaster.R;
import com.ewida.mealmaster.auth.login.LoginContracts;
import com.ewida.mealmaster.data.model.User;
import com.ewida.mealmaster.data.repository.meals_repo.MealsRepository;
import com.ewida.mealmaster.data.repository.user_repo.UserRepository;
import com.ewida.mealmaster.utils.Constants;
import com.ewida.mealmaster.utils.enums.FormErrors;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginPresenter implements LoginContracts.Presenter {
    private final LoginContracts.View loginView;
    private final UserRepository userRepo;
    private final MealsRepository mealsRepo;
    private final ObservableArrayList<FormErrors> loginFormErrors;

    public LoginPresenter(LoginContracts.View loginView, MealsRepository mealsRepo, UserRepository userRepo) {
        this.mealsRepo = mealsRepo;
        this.userRepo = userRepo;
        this.loginView = loginView;
        this.loginFormErrors = new ObservableArrayList<>();
    }

    public ObservableArrayList<FormErrors> getFormErrors() {
        return this.loginFormErrors;
    }

    @Override
    public void handleLoginClick(String email, String password) {
        if (isCredentialValid(email, password)) {
            loginView.showLoaderOnLoginButton();
            loginWithEmailAndPassword(email, password);
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

    private void loginWithEmailAndPassword(String email, String password) {
        userRepo.login(email, password).addOnSuccessListener(authResult -> {
            getUsernameFromDatabase(authResult.getUser().getUid());
        }).addOnFailureListener(error -> {
            loginView.showErrorMessage(error.getMessage());
        });
    }

    private void getUsernameFromDatabase(String id) {
        userRepo.getUserByID(id).get().addOnSuccessListener(dataSnapshot -> {
            String username = dataSnapshot.getValue(User.class).getName();
            userRepo.setCurrentUserName(username);
            userRepo.setCurrentUserId(id);
            syncUserData(id);
        }).addOnFailureListener(error -> {
            loginView.showErrorMessage(error.getMessage());
        });
    }

    @SuppressLint("CheckResult")
    private void syncUserData(String userId) {
        mealsRepo.syncUserData(userId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                loginView::navigateToHomeScreen,
                error -> loginView.showErrorMessage(error.getMessage())
        );
    }

    private void insertUserInDatabase(User user) {
        userRepo.saveUserData(user).addOnSuccessListener(unused -> {
            userRepo.setCurrentUserName(user.getName());
            userRepo.setCurrentUserId(user.getId());
            syncUserData(user.getId());
        }).addOnFailureListener(error -> {
            loginView.showErrorMessage(error.getMessage());
        });
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
    public void handleGoogleAuthResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            userRepo.authWithGoogle(account).addOnSuccessListener(authResult -> {
                userRepo.setCurrentUserName(authResult.getUser().getDisplayName());
                userRepo.setCurrentUserId(authResult.getUser().getUid());
                insertUserInDatabase(new User(authResult.getUser().getUid(), authResult.getUser().getDisplayName()));
            }).addOnFailureListener(error -> {
                loginView.showErrorMessage(error.getMessage());
            });
        } catch (ApiException e) {
            loginView.showErrorMessage(e.getMessage());
        }
    }
}
