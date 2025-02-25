package com.ewida.mealmaster.meal_details.view;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.ewida.mealmaster.R;
import com.ewida.mealmaster.data.datasource.local.MealsLocalDataSourceImpl;
import com.ewida.mealmaster.data.datasource.local.UserLocalDataSourceImpl;
import com.ewida.mealmaster.data.datasource.remote.MealsRemoteDataSourceImpl;
import com.ewida.mealmaster.data.datasource.remote.UserRemoteDataSourceImpl;
import com.ewida.mealmaster.data.model.Ingredient;
import com.ewida.mealmaster.data.model.Meal;

import com.ewida.mealmaster.data.repository.meals_repo.MealsRepositoryImpl;
import com.ewida.mealmaster.data.repository.user_repo.UserRepositoryImpl;
import com.ewida.mealmaster.databinding.ActivityMealDetailsBinding;
import com.ewida.mealmaster.meal_details.MealDetailsContracts;
import com.ewida.mealmaster.meal_details.presenter.MealDetailsPresenter;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;

import java.util.Calendar;
import java.util.List;

public class MealDetailsActivity extends AppCompatActivity implements MealDetailsContracts.View {

    public static final String MEAL_ID_EXTRA = "MEAL_ID_EXTRA";
    public static final String MEAL_OBJECT_EXTRA = "MEAL_OBJECT_EXTRA";
    private ActivityMealDetailsBinding binding;
    private MealDetailsContracts.Presenter presenter;
    private Meal meal;
    private boolean isSaved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMealDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        presenter = new MealDetailsPresenter(
                this,
                MealsRepositoryImpl.getInstance(
                        MealsRemoteDataSourceImpl.getInstance(),
                        MealsLocalDataSourceImpl.getInstance(this)
                ),
                UserRepositoryImpl.getInstance(
                        UserRemoteDataSourceImpl.getInstance(FirebaseAuth.getInstance(), FirebaseDatabase.getInstance()),
                        UserLocalDataSourceImpl.getInstance(this)
                )
        );

        initClicks();
        getMeal(getIntent().getStringExtra(MEAL_ID_EXTRA));
    }

    private void initClicks() {
        binding.icBack.setOnClickListener(view -> finish());
        binding.icSave.setOnClickListener(view -> {
            if (isSaved) {
                presenter.unSaveMeal(meal);
            } else {
                presenter.saveMeal(meal);
            }
            setSavedIcon(!isSaved);
        });
        binding.icPlan.setOnClickListener(view -> {
            showDatePickerDialog();
        });
    }

    private void getMeal(String id) {
        if (id == null) {
            Meal meal = getIntent().getParcelableExtra(MEAL_OBJECT_EXTRA);
            if (meal != null) {
                this.meal = meal;
                showMealDetails(meal);
            }
        } else {
            presenter.getMealById(id);
        }
    }

    @Override
    public void showMealDetails(Meal meal) {
        this.meal = meal;
        presenter.isMealSaved(meal.getIdMeal());
        Glide.with(binding.ivMealThumbnail)
                .load(meal.getStrMealThumb())
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder)
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

    @Override
    public void setSavedIcon(boolean isSaved) {
        this.isSaved = isSaved;
        binding.icSave.setImageResource(isSaved ? R.drawable.ic_saved : R.drawable.ic_un_saved);
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
    public void showMessage(String msg) {
        Snackbar.make(binding.getRoot(), msg, Snackbar.LENGTH_SHORT).show();
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = selectedDay + "-" + (selectedMonth + 1) + "-" + selectedYear;
                    presenter.planMeal(this.meal, selectedDate);
                },
                year, month, day
        );

        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        calendar.add(Calendar.MONTH, 1);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

}