package com.ewida.mealmaster.auth.register.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import androidx.databinding.ObservableArrayList;
import com.ewida.mealmaster.R;
import com.ewida.mealmaster.auth.register.RegisterContracts;
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

public class RegisterPresenter implements RegisterContracts.Presenter {

    private final RegisterContracts.View registerView;
    private final UserRepository userRepo;
    private final MealsRepository mealsRepo;
    private final ObservableArrayList<FormErrors> registerFormErrors;

    public RegisterPresenter(RegisterContracts.View registerView, UserRepository repo, MealsRepository mealsRepo) {
        this.registerView = registerView;
        this.userRepo = repo;
        this.mealsRepo = mealsRepo;
        this.registerFormErrors = new ObservableArrayList<>();
    }

    public ObservableArrayList<FormErrors> getFormErrors() {
        return registerFormErrors;
    }

    @Override
    public void handleCreateAccountClick(String fullName, String email, String password) {
        if (isCredentialValid(fullName, email, password)) {
            registerView.showLoaderOnCreateAccountButton();
            registerWithEmailAndPassword(fullName, email, password);
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

    private void registerWithEmailAndPassword(String fullName, String email, String password) {
        userRepo.register(email, password).addOnSuccessListener(authResult -> {
            User user = new User(authResult.getUser().getUid(), fullName);
            insertUserInDatabase(user);
        }).addOnFailureListener(error -> registerView.showErrorMessage(error.getMessage()));
    }

    private void insertUserInDatabase(User user) {
        userRepo.saveUserData(user).addOnSuccessListener(unused -> {
            userRepo.setCurrentUserName(user.getName());
            userRepo.setCurrentUserId(user.getId());
            syncUserData(user.getId());
        }).addOnFailureListener(error -> {
            registerView.showErrorMessage(error.getMessage());
        });
    }

    @SuppressLint("CheckResult")
    private void syncUserData(String userId) {
        mealsRepo.syncUserData(userId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                registerView::navigateToHomeScreen,
                error -> registerView.showErrorMessage(error.getMessage())
        );
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
            userRepo.authWithGoogle(account).addOnSuccessListener(authResult -> {
                userRepo.setCurrentUserName(authResult.getUser().getDisplayName());
                userRepo.setCurrentUserId(authResult.getUser().getUid());
                insertUserInDatabase(new User(authResult.getUser().getUid(),authResult.getUser().getDisplayName()));
            }).addOnFailureListener(error -> registerView.showErrorMessage(error.getMessage()));
        } catch (ApiException exception) {
            registerView.showErrorMessage(exception.getMessage());
        }
    }

}
