package com.ewida.mealmaster.main.profile.presenter;

import android.annotation.SuppressLint;

import com.ewida.mealmaster.data.repository.user_repo.UserRepository;
import com.ewida.mealmaster.main.profile.ProfileContracts;
import com.ewida.mealmaster.utils.NetworkUtils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class ProfilePresenter implements ProfileContracts.Presenter {

    private final UserRepository userRepo;
    private final ProfileContracts.View view;

    public ProfilePresenter(ProfileContracts.View view, UserRepository userRepo) {
        this.userRepo = userRepo;
        this.view = view;
    }


    @SuppressLint("CheckResult")
    @Override
    public void getUserProfile() {
        userRepo.getUserProfile().observeOn(AndroidSchedulers.mainThread()).subscribe(
                view::showUserData,
                error -> view.showMessage(error.getMessage())
        );
    }

    @SuppressLint("CheckResult")
    @Override
    public void backupUserData() {
        if(NetworkUtils.isNetworkAvailable()){
            userRepo.backupUserData().observeOn(AndroidSchedulers.mainThread()).subscribe(
                    view::onBackupCompleted,
                    error-> view.showMessage(error.getMessage())
            );
        }else{
            view.showMessage("Can't backup your data, no internet connection");
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void logout() {
        userRepo.endUserSession().observeOn(AndroidSchedulers.mainThread()).subscribe(
                view::onLogoutProcessCompleted,
                error-> view.showMessage("An error occured while logging out, try again later")
        );
    }

    @Override
    public boolean isGuest() {
        return userRepo.getCurrentUserId() == null;
    }
}
