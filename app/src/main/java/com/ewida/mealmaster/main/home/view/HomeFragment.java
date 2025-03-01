package com.ewida.mealmaster.main.home.view;

import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.ewida.mealmaster.R;
import com.ewida.mealmaster.data.datasource.local.MealsLocalDataSourceImpl;
import com.ewida.mealmaster.data.datasource.local.UserLocalDataSourceImpl;
import com.ewida.mealmaster.data.datasource.remote.MealsRemoteDataSourceImpl;
import com.ewida.mealmaster.data.datasource.remote.UserRemoteDataSourceImpl;
import com.ewida.mealmaster.data.repository.meals_repo.MealsRepositoryImpl;
import com.ewida.mealmaster.data.repository.user_repo.UserRepositoryImpl;
import com.ewida.mealmaster.databinding.FragmentHomeBinding;
import com.ewida.mealmaster.data.model.CategoryMeal;
import com.ewida.mealmaster.data.model.Meal;
import com.ewida.mealmaster.main.home.HomeContracts;
import com.ewida.mealmaster.main.home.presenter.HomePresenter;
import com.ewida.mealmaster.meal_details.view.MealDetailsActivity;
import com.ewida.mealmaster.search.view.SearchActivity;
import com.ewida.mealmaster.utils.NetworkUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class HomeFragment extends Fragment implements HomeContracts.View, HomeMealsAdapter.OnMealClickListener, NetworkUtils.NetworkCallbacksListener {

    private FragmentHomeBinding binding;
    private HomeContracts.Presenter presenter;
    private Meal randomMeal;
    private NetworkUtils networkUtils;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        presenter = new HomePresenter(
                this,
                MealsRepositoryImpl.getInstance(
                        MealsRemoteDataSourceImpl.getInstance(),
                        MealsLocalDataSourceImpl.getInstance(requireContext())
                ),
                UserRepositoryImpl.getInstance(
                        UserRemoteDataSourceImpl.getInstance(FirebaseAuth.getInstance(), FirebaseDatabase.getInstance()),
                        UserLocalDataSourceImpl.getInstance(requireActivity()),
                        MealsLocalDataSourceImpl.getInstance(requireActivity())
                )
        );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.getUsername();
        if(!NetworkUtils.isNetworkAvailable()) onConnectionUnAvailable();
        networkUtils = new NetworkUtils(requireContext(),this);
        networkUtils.register();
        initClicks();
    }

    private void initClicks() {
        binding.randomMealCard.setOnClickListener(view -> {
            Intent mealDetailsIntent = new Intent(requireActivity(), MealDetailsActivity.class);
            mealDetailsIntent.putExtra(MealDetailsActivity.MEAL_OBJECT_EXTRA, (Parcelable) randomMeal);
            startActivity(mealDetailsIntent);
        });

        binding.btnSearch.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), SearchActivity.class));
        });
    }

    @Override
    public void showUsername(String username) {
        binding.tvUsername.setText(username);
    }

    @Override
    public void showRandomMeal(Meal meal) {
        this.randomMeal = meal;
        networkUtils.unregister();
        Glide.with(binding.ivRadnomMeal)
                .load(meal.getStrMealThumb())
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.ivRadnomMeal);
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

    @Override
    public void onConnectionAvailable() {
        presenter.getRandomMeal();
        presenter.getVegetarianMeals();
        presenter.getDesserts();
    }

    @Override
    public void onConnectionUnAvailable() {
        Snackbar.make(binding.getRoot(), R.string.no_internet_connection_available, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        networkUtils.unregister();
    }
}