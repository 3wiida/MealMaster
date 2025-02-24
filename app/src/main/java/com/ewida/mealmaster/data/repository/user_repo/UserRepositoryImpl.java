package com.ewida.mealmaster.data.repository.user_repo;

import com.ewida.mealmaster.data.datasource.local.UserLocalDataSource;
import com.ewida.mealmaster.data.datasource.remote.UserRemoteDataSource;
import com.ewida.mealmaster.data.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;

public class UserRepositoryImpl implements UserRepository {

    private final UserRemoteDataSource userRemoteDataSource;
    private final UserLocalDataSource userLocalDataSource;
    private static UserRepository instance;


    private UserRepositoryImpl(
            UserRemoteDataSource userRemoteDataSource,
            UserLocalDataSource userLocalDataSource
    ) {
        this.userRemoteDataSource = userRemoteDataSource;
        this.userLocalDataSource = userLocalDataSource;
    }

    public static UserRepository getInstance(
            UserRemoteDataSource userRemoteDataSource,
            UserLocalDataSource userLocalDataSource
    ) {
        if (instance == null) {
            instance = new UserRepositoryImpl(userRemoteDataSource, userLocalDataSource);
        }
        return instance;
    }

    @Override
    public Task<AuthResult> register(String email, String password) {
        return userRemoteDataSource.signUpWithEmailAndPassword(email, password);
    }

    @Override
    public Task<AuthResult> login(String email, String password) {
        return userRemoteDataSource.signInWithEmailAndPassword(email, password);
    }

    @Override
    public Task<AuthResult> authWithGoogle(GoogleSignInAccount account) {
        return userRemoteDataSource.authWithGoogle(account);
    }

    @Override
    public Task<Void> saveUserData(User user) {
        return userRemoteDataSource.insertUser(user);
    }

    @Override
    public DatabaseReference getUserByID(String id) {
        return userRemoteDataSource.getUserByID(id);
    }

    @Override
    public String getCurrentUserName() {
        return userLocalDataSource.getCurrentUserName();
    }

    @Override
    public void setCurrentUserName(String name) {
        userLocalDataSource.setCurrentUserName(name);
    }

    @Override
    public String getCurrentUserId() {
        return userLocalDataSource.getCurrentUserId();
    }

    @Override
    public void setCurrentUserId(String id) {
        userLocalDataSource.setCurrentUserId(id);
    }

    @Override
    public Boolean isFirstTime() {
        return userLocalDataSource.isFirstTime();
    }

    @Override
    public void setFirstTime(boolean isFirstTime) {
        userLocalDataSource.setFirstTime(isFirstTime);
    }

    @Override
    public Boolean isAfterAuth() {
        return userLocalDataSource.isAfterAuth();
    }

    @Override
    public void setAfterAuth(boolean isAfterAuth) {
        userLocalDataSource.setAfterAuth(isAfterAuth);
    }
}
