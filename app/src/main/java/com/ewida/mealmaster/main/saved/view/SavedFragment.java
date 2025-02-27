package com.ewida.mealmaster.main.saved.view;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ewida.mealmaster.R;
import com.ewida.mealmaster.auth.AuthActivity;
import com.ewida.mealmaster.data.datasource.local.MealsLocalDataSourceImpl;
import com.ewida.mealmaster.data.datasource.local.UserLocalDataSourceImpl;
import com.ewida.mealmaster.data.datasource.remote.MealsRemoteDataSourceImpl;
import com.ewida.mealmaster.data.datasource.remote.UserRemoteDataSourceImpl;
import com.ewida.mealmaster.data.model.Meal;
import com.ewida.mealmaster.data.repository.meals_repo.MealsRepositoryImpl;
import com.ewida.mealmaster.data.repository.user_repo.UserRepositoryImpl;
import com.ewida.mealmaster.databinding.FragmentSavedBinding;
import com.ewida.mealmaster.main.saved.SavedMealsContracts;
import com.ewida.mealmaster.main.saved.presenter.SavedMealsPresenter;
import com.ewida.mealmaster.meal_details.view.MealDetailsActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.List;


public class SavedFragment extends Fragment implements SavedMealsContracts.View,SavedMealsAdapter.OnMealClickListener {

    private FragmentSavedBinding binding;
    private SavedMealsContracts.Presenter presenter;
    private SavedMealsAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSavedBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new SavedMealsPresenter(
                this,
                MealsRepositoryImpl.getInstance(
                        MealsRemoteDataSourceImpl.getInstance(),
                        MealsLocalDataSourceImpl.getInstance(requireContext())),
                UserRepositoryImpl.getInstance(
                        UserRemoteDataSourceImpl.getInstance(FirebaseAuth.getInstance(), FirebaseDatabase.getInstance()),
                        UserLocalDataSourceImpl.getInstance(requireContext()),
                        MealsLocalDataSourceImpl.getInstance(requireActivity())
                )
        );
        initViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(presenter.isGuest()){
            showLoginRequiredLayout();
        }else{
            binding.progressBar.setVisibility(View.VISIBLE);
            presenter.getSavedMeals();
        }
    }

    private void initViews(){
        adapter = new SavedMealsAdapter(this);
        binding.rvSavedMeals.setAdapter(adapter);
    }

    private void showLoginRequiredLayout(){
        binding.loginRequiredLayout.setVisibility(View.VISIBLE);
        binding.loginRequiredLayout.findViewById(R.id.btnLogin).setOnClickListener(view->{
            navigateToAuthActivity();
        });
    }

    private void navigateToAuthActivity(){
        requireActivity().startActivity(new Intent(requireActivity(), AuthActivity.class));
    }

    @Override
    public void showSavedMeals(List<Meal> meals) {
        binding.progressBar.setVisibility(View.GONE);
        binding.emptyState.setVisibility(View.GONE);
        List<Meal> newList = new ArrayList<>(meals);
        adapter.submitList(newList);
    }

    @Override
    public void showMessage(String msg) {
        Snackbar.make(binding.getRoot(), msg, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showEmptyState() {
        binding.progressBar.setVisibility(View.GONE);
        binding.emptyState.setVisibility(View.VISIBLE);
    }

    @Override
    public void showUnsavedSnackBar(Meal meal) {
        Snackbar snackbar = Snackbar
                .make(binding.getRoot(), "Meal unsaved successfully", Snackbar.LENGTH_LONG)
                .setAction("Undo", view -> {
                    presenter.saveMeal(meal);
                });
        snackbar.show();
    }

    @Override
    public void onMealClicked(Meal meal) {
        Intent mealDetailsIntent = new Intent(requireActivity(), MealDetailsActivity.class);
        mealDetailsIntent.putExtra(MealDetailsActivity.MEAL_OBJECT_EXTRA, (Parcelable) meal);
        startActivity(mealDetailsIntent);
    }

    @Override
    public void onUnSaveClicked(Meal meal) {
        presenter.unSaveMeal(meal);
    }
}