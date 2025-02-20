package com.ewida.mealmaster.meal_details.view;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.ewida.mealmaster.R;
import com.ewida.mealmaster.data.datasource.remote.MealsRemoteDataSourceImpl;
import com.ewida.mealmaster.data.model.Ingredient;
import com.ewida.mealmaster.data.model.Meal;
import com.ewida.mealmaster.data.repository.MealsRepositoryImpl;
import com.ewida.mealmaster.databinding.ActivityMealDetailsBinding;
import com.ewida.mealmaster.meal_details.MealDetailsContracts;
import com.ewida.mealmaster.meal_details.presenter.MealDetailsPresenter;
import com.google.android.material.snackbar.Snackbar;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;

import java.util.List;

public class MealDetailsActivity extends AppCompatActivity implements MealDetailsContracts.View {

    private ActivityMealDetailsBinding binding;
    private MealDetailsContracts.Presenter presenter;
    public static final String MEAL_ID_EXTRA = "MEAL_ID_EXTRA";
    public static final String MEAL_OBJECT_EXTRA = "MEAL_OBJECT_EXTRA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMealDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        presenter = new MealDetailsPresenter(this, MealsRepositoryImpl.getInstance(MealsRemoteDataSourceImpl.getInstance()));
        initClicks();
        getMeal(getIntent().getStringExtra(MEAL_ID_EXTRA));
    }

    private void initClicks() {
        binding.icBack.setOnClickListener(view -> finish());
    }

    private void getMeal(String id) {
        if (id == null) {
            Meal meal = getIntent().getParcelableExtra(MEAL_OBJECT_EXTRA);
            if (meal != null)
                showMealDetails(meal);
        } else {
            presenter.getMealById(id);
        }
    }

    @Override
    public void showMealDetails(Meal meal) {
        Glide.with(binding.ivMealThumbnail)
                .load(meal.getStrMealThumb())
                .placeholder(R.drawable.image_placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.ivMealThumbnail);
        binding.setMeal(meal);
        stopLoadingAndShowData();
        initIngredientsRv(meal.getIngredients());
        initVideoPlayer(meal.getStrYoutube());
    }

    private void stopLoadingAndShowData() {
        binding.loadingLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out_anim));
        new Handler().postDelayed(() -> {
            binding.loadingLayout.setVisibility(GONE);
            binding.dataConstraint.setVisibility(VISIBLE);
            binding.dataConstraint.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in_anim));
        }, 300L);
    }

    private void initIngredientsRv(List<Ingredient> ingredientList) {
        IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(ingredientList);
        binding.rvIngredients.setAdapter(ingredientsAdapter);
    }

    private void initVideoPlayer(String videoId) {
        binding.youtubeVideoPlayer.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                if (videoId != null) {
                    youTubePlayer.loadVideo(videoId, 0);
                    youTubePlayer.pause();
                }
            }
        });
    }

    @Override
    public void showErrorMessage(String errorMsg) {
        Snackbar.make(binding.getRoot(), errorMsg, Snackbar.LENGTH_SHORT).show();
    }
}