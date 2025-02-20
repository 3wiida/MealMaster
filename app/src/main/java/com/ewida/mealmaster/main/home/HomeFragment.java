package com.ewida.mealmaster.main.home;

import static android.view.View.VISIBLE;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.ewida.mealmaster.R;
import com.ewida.mealmaster.data.datasource.remote.MealsRemoteDataSourceImpl;
import com.ewida.mealmaster.data.repository.MealsRepositoryImpl;
import com.ewida.mealmaster.databinding.FragmentHomeBinding;
import com.ewida.mealmaster.data.model.CategoryMeal;
import com.ewida.mealmaster.data.model.Meal;
import com.ewida.mealmaster.meal_details.view.MealDetailsActivity;
import com.ewida.mealmaster.utils.Constants;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class HomeFragment extends Fragment implements HomeViewContract, HomeMealsAdapter.OnMealClickListener {

    private FragmentHomeBinding binding;
    private HomePresenterContract presenter;
    private Meal randomMeal;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        presenter = new HomePresenter(this, MealsRepositoryImpl.getInstance(MealsRemoteDataSourceImpl.getInstance()));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.getRandomMeal();
        presenter.getVegetarianMeals();
        presenter.getDesserts();
        String username = getContext().getSharedPreferences(Constants.SharedPref.PREFS_FILE_NAME, Context.MODE_PRIVATE).getString(Constants.SharedPref.USER_NAME_KEY, "Anonymous Guest");
        binding.tvUsername.setText(username);
        initClicks();
    }

    private void initClicks() {
        binding.randomMealCard.setOnClickListener(view -> {
            Intent mealDetailsIntent = new Intent(getActivity(), MealDetailsActivity.class);
            mealDetailsIntent.putExtra(MealDetailsActivity.MEAL_OBJECT_EXTRA, randomMeal);
            startActivity(mealDetailsIntent);
        });
    }

    @Override
    public void showRandomMeal(Meal meal) {
        this.randomMeal = meal;
        Glide.with(binding.ivRadnomMeal).load(meal.getStrMealThumb()).placeholder(R.drawable.image_placeholder).transition(DrawableTransitionOptions.withCrossFade()).into(binding.ivRadnomMeal);
        binding.setRandomMeal(meal);
        binding.randomMealShimmer.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_out_anim));
        new Handler().postDelayed(() -> {
            binding.randomMealCard.setVisibility(VISIBLE);
            binding.randomMealCard.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_anim));
        }, 300L);
    }

    @Override
    public void showVegetarianMeals(List<CategoryMeal> meals) {
        HomeMealsAdapter adapter = new HomeMealsAdapter(meals, true, this);
        binding.rvVegetarianMeals.setAdapter(adapter);
        binding.vegetarianFoodShimmer.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_out_anim));
        new Handler().postDelayed(() -> {
            binding.rvVegetarianMeals.setVisibility(VISIBLE);
            binding.rvVegetarianMeals.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_anim));
        }, 300L);
    }

    @Override
    public void showDesserts(List<CategoryMeal> desserts) {
        HomeMealsAdapter adapter = new HomeMealsAdapter(desserts, false, this);
        binding.rvDesserts.setAdapter(adapter);
        binding.dessertsShimmer.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_out_anim));
        new Handler().postDelayed(() -> {
            binding.rvDesserts.setVisibility(VISIBLE);
            binding.rvDesserts.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_anim));
        }, 300L);
    }

    @Override
    public void showErrorMessage(String errorMSg) {
        Snackbar.make(binding.getRoot(), errorMSg, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onMealClicked(CategoryMeal meal) {
        Intent mealDetailsIntent = new Intent(getActivity(), MealDetailsActivity.class);
        mealDetailsIntent.putExtra(MealDetailsActivity.MEAL_ID_EXTRA, meal.getIdMeal());
        startActivity(mealDetailsIntent);
    }
}