package com.ewida.mealmaster.auth.login.presenter;


import android.content.Context;
import android.content.Intent;
import androidx.databinding.ObservableArrayList;
import com.ewida.mealmaster.R;
import com.ewida.mealmaster.auth.login.LoginContracts;
import com.ewida.mealmaster.data.model.User;
import com.ewida.mealmaster.data.repository.user_repo.UserRepository;
import com.ewida.mealmaster.utils.Constants;
import com.ewida.mealmaster.utils.enums.FormErrors;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginPresenter implements LoginContracts.Presenter {
    private final LoginContracts.View loginView;
    private final UserRepository repo;
    private final ObservableArrayList<FormErrors> loginFormErrors;

    public LoginPresenter(LoginContracts.View loginView, UserRepository repo) {
        this.loginView = loginView;
        this.repo = repo;
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
        repo.login(email, password).addOnSuccessListener(authResult -> {
            getUsernameFromDatabase(authResult.getUser().getUid());
        }).addOnFailureListener(error -> {
            loginView.showErrorMessage(error.getMessage());
        });
    }

    private void getUsernameFromDatabase(String id) {
        repo.getUserByID(id).get().addOnSuccessListener(dataSnapshot -> {
            String username = dataSnapshot.getValue(User.class).getName();
            repo.setCurrentUserName(username);
            repo.setCurrentUserId(id);
            loginView.navigateToHomeScreen();
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
            repo.authWithGoogle(account).addOnSuccessListener(authResult -> {
                repo.setCurrentUserName(authResult.getUser().getDisplayName());
                repo.setCurrentUserId(authResult.getUser().getUid());
                loginView.navigateToHomeScreen();
            }).addOnFailureListener(error -> {
                loginView.showErrorMessage(error.getMessage());
            });
        } catch (ApiException e) {
            loginView.showErrorMessage(e.getMessage());
        }
    }
}
