package com.ewida.mealmaster.explore_meals.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AnimationUtils;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ewida.mealmaster.R;
import com.ewida.mealmaster.data.datasource.local.MealsLocalDataSourceImpl;
import com.ewida.mealmaster.data.datasource.remote.MealsRemoteDataSourceImpl;
import com.ewida.mealmaster.data.model.CategoryMeal;
import com.ewida.mealmaster.data.repository.meals_repo.MealsRepositoryImpl;
import com.ewida.mealmaster.databinding.ActivityExploreItemMealsBinding;
import com.ewida.mealmaster.explore_meals.ExploreMealsContracts;
import com.ewida.mealmaster.explore_meals.presenter.ExploreMealsPresenter;
import com.ewida.mealmaster.meal_details.view.MealDetailsActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ExploreMeals extends AppCompatActivity implements ExploreMealsContracts.View, ExploreMealsAdapter.OnMealClickListener {

    public static final String EXPLORE_TYPE_EXTRA = "EXPLORE_TYPE_EXTRA";
    public static final String EXPLORE_QUERY_EXTRA = "EXPLORE_QUERY_EXTRA";
    private ActivityExploreItemMealsBinding binding;
    private ExploreMealsContracts.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityExploreItemMealsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        presenter = new ExploreMealsPresenter(
                this,
                MealsRepositoryImpl.getInstance(
                        MealsRemoteDataSourceImpl.getInstance(),
                        MealsLocalDataSourceImpl.getInstance(this)
                )
        );
        initViews();
        initClicks();
        getMeals();
    }

    @SuppressLint("SetTextI18n")
    private void initViews() {
        String query = getIntent().getStringExtra(EXPLORE_QUERY_EXTRA);
        binding.tvItemLabel.setText(query + " Meals");
    }

    private void getMeals() {
        ExploreMealsContracts.ExploreType type = (ExploreMealsContracts.ExploreType) getIntent().getSerializableExtra(EXPLORE_TYPE_EXTRA);
        String query = getIntent().getStringExtra(EXPLORE_QUERY_EXTRA);
        presenter.getMeals(type, query);
    }

    private void initClicks() {
        binding.icBack.setOnClickListener(view -> finish());
    }

    @Override
    public void showMealsList(List<CategoryMeal> meals) {
        ExploreMealsAdapter mealsAdapter = new ExploreMealsAdapter(meals, this);
        binding.rvMeals.setAdapter(mealsAdapter);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            binding.rvMeals.setVisibility(View.VISIBLE);
            binding.rvMeals.startAnimation(AnimationUtils.loadAnimation(this,R.anim.fade_in_anim));
        }, 300L);
    }

    @Override
    public void showErrorMsg(String errorMsg) {
        Snackbar.make(binding.getRoot(), errorMsg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void hideLoadingState() {
        binding.progressBar.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out_anim));
    }

    @Override
    public void onMealClicked(CategoryMeal meal) {
        Intent mealDetailsIntent = new Intent(this, MealDetailsActivity.class);
        mealDetailsIntent.putExtra(MealDetailsActivity.MEAL_ID_EXTRA, meal.getIdMeal());
        startActivity(mealDetailsIntent);
    }
}