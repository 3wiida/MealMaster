package com.ewida.mealmaster.data.repository.user_repo;

import com.ewida.mealmaster.data.model.Profile;
import com.ewida.mealmaster.data.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;

import io.reactivex.rxjava3.core.Single;

public interface UserRepository {
    Task<AuthResult> register(String email, String password);

    Task<AuthResult> login(String email, String password);

    Task<AuthResult> authWithGoogle(GoogleSignInAccount account);

    Task<Void> saveUserData(User user);

    DatabaseReference getUserByID(String id);

    String getCurrentUserName();

    void setCurrentUserName(String name);

    String getCurrentUserId();

    void setCurrentUserId(String id);

    Boolean isFirstTime();

    void setFirstTime(boolean isFirstTime);

    Single<Profile> getUserProfile();
}
