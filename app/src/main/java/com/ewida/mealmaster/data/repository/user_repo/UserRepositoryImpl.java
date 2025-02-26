package com.ewida.mealmaster.data.repository.user_repo;

import android.annotation.SuppressLint;

import com.ewida.mealmaster.data.datasource.local.MealsLocalDataSource;
import com.ewida.mealmaster.data.datasource.local.UserLocalDataSource;
import com.ewida.mealmaster.data.datasource.remote.UserRemoteDataSource;
import com.ewida.mealmaster.data.model.Profile;
import com.ewida.mealmaster.data.model.User;
import com.ewida.mealmaster.data.model.UserStatistics;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UserRepositoryImpl implements UserRepository {

    private final UserRemoteDataSource userRemoteDataSource;
    private final UserLocalDataSource userLocalDataSource;
    private final MealsLocalDataSource mealsLocalDataSource;
    private static UserRepository instance;


    private UserRepositoryImpl(
            UserRemoteDataSource userRemoteDataSource,
            UserLocalDataSource userLocalDataSource,
            MealsLocalDataSource mealsLocalDataSource
    ) {
        this.userRemoteDataSource = userRemoteDataSource;
        this.userLocalDataSource = userLocalDataSource;
        this.mealsLocalDataSource = mealsLocalDataSource;
    }

    public static UserRepository getInstance(
            UserRemoteDataSource userRemoteDataSource,
            UserLocalDataSource userLocalDataSource,
            MealsLocalDataSource mealsLocalDataSource
    ) {
        if (instance == null) {
            instance = new UserRepositoryImpl(userRemoteDataSource, userLocalDataSource, mealsLocalDataSource);
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

    @SuppressLint("CheckResult")
    @Override
    public Single<Profile> getUserProfile() {
        return Single.create(emitter -> {
            String username = userLocalDataSource.getCurrentUserName();
            String userEmail = userRemoteDataSource.getCurrentUserEmail();
            mealsLocalDataSource.getUserStatistics().subscribeOn(Schedulers.io()).subscribe(
                    data -> {
                        Profile profile = new Profile(username, userEmail, data);
                        emitter.onSuccess(profile);
                    },
                    emitter::onError
            );
        });
    }
}
