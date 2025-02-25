package com.ewida.mealmaster.main.profile.presenter;

import android.annotation.SuppressLint;

import com.ewida.mealmaster.data.repository.user_repo.UserRepository;
import com.ewida.mealmaster.main.profile.ProfileContracts;

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
}
