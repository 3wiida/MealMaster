package com.ewida.mealmaster.main.profile.view;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ewida.mealmaster.auth.AuthActivity;
import com.ewida.mealmaster.data.datasource.local.MealsLocalDataSourceImpl;
import com.ewida.mealmaster.data.datasource.local.UserLocalDataSourceImpl;
import com.ewida.mealmaster.data.datasource.remote.UserRemoteDataSourceImpl;
import com.ewida.mealmaster.data.model.Profile;
import com.ewida.mealmaster.data.repository.user_repo.UserRepositoryImpl;
import com.ewida.mealmaster.databinding.FragmentProfileBinding;
import com.ewida.mealmaster.main.profile.ProfileContracts;
import com.ewida.mealmaster.main.profile.presenter.ProfilePresenter;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class ProfileFragment extends Fragment implements ProfileContracts.View, LogoutDialog.OnLogoutConfirmedListener {

    private FragmentProfileBinding binding;
    private ProfileContracts.Presenter presenter;
    private LogoutDialog logoutDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new ProfilePresenter(
                this,
                UserRepositoryImpl.getInstance(
                        UserRemoteDataSourceImpl.getInstance(FirebaseAuth.getInstance(), FirebaseDatabase.getInstance()),
                        UserLocalDataSourceImpl.getInstance(requireActivity()),
                        MealsLocalDataSourceImpl.getInstance(requireActivity())
                )
        );
        presenter.getUserProfile();
        initViews();
        initClicks();
    }

    private void initViews() {
        binding.btnBackup.setVisibility(presenter.isGuest() ? GONE : VISIBLE);
    }

    private void initClicks() {
        binding.btnBackup.setOnClickListener(view -> {
            binding.btnBackup.setLoading(true);
            presenter.backupUserData();
        });

        binding.btnLogout.setOnClickListener(view -> {
            logoutDialog = new LogoutDialog(requireContext(), this);
            logoutDialog.show();
        });
    }

    @Override
    public void showUserData(Profile userProfile) {
        binding.setUserProfile(userProfile);
    }

    @Override
    public void showMessage(String msg) {
        binding.btnBackup.setLoading(false);
        Snackbar.make(binding.getRoot(), msg, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onBackupCompleted() {
        binding.btnBackup.setLoading(false);
        showMessage("Backup Completed Successfully");
    }

    @Override
    public void onLogoutConfirmed() {
        presenter.logout();
    }

    @Override
    public void onLogoutProcessCompleted() {
        requireActivity().startActivity(new Intent(requireActivity(), AuthActivity.class));
        requireActivity().finish();
    }
}