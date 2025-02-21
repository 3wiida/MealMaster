package com.ewida.mealmaster.main.explore.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ewida.mealmaster.R;
import com.ewida.mealmaster.data.datasource.remote.MealsRemoteDataSourceImpl;
import com.ewida.mealmaster.data.model.Category;
import com.ewida.mealmaster.data.model.ExploreItem;
import com.ewida.mealmaster.data.repository.MealsRepositoryImpl;
import com.ewida.mealmaster.databinding.FragmentExploreBinding;
import com.ewida.mealmaster.main.explore.ExploreContracts;
import com.ewida.mealmaster.main.explore.presenter.ExplorePresenter;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ExploreFragment extends Fragment implements ExploreContracts.View {

    private FragmentExploreBinding binding;
    private ExploreContracts.Presenter presenter;
    private ExploreItemAdapter categoriesAdapter;
    private ExploreItemAdapter areasAdapter;
    private ExploreItemAdapter ingredientsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentExploreBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new ExplorePresenter(this, MealsRepositoryImpl.getInstance(MealsRemoteDataSourceImpl.getInstance()));
        presenter.getAllCategories();
        presenter.getAllAreas();
        presenter.getAllIngredients();
    }

    @Override
    public void showAllCategories(List<ExploreItem> categories) {
        categoriesAdapter = new ExploreItemAdapter(categories);
        binding.rvCategories.setAdapter(categoriesAdapter);
    }

    @Override
    public void showAllAreas(List<ExploreItem> areas) {
        areasAdapter = new ExploreItemAdapter(areas);
        binding.rvAreas.setAdapter(areasAdapter);
    }

    @Override
    public void showAllIngredients(List<ExploreItem> ingredients) {
        ingredientsAdapter = new ExploreItemAdapter(ingredients);
        binding.rvIngredients.setAdapter(ingredientsAdapter);
    }

    @Override
    public void showErrorMessage(String errorMsg) {
        Snackbar.make(binding.getRoot(), errorMsg, Snackbar.LENGTH_LONG).show();
    }

}