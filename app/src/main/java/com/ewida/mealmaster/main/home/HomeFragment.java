package com.ewida.mealmaster.main.home;

import static android.view.View.VISIBLE;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.ewida.mealmaster.R;
import com.ewida.mealmaster.databinding.FragmentHomeBinding;
import com.ewida.mealmaster.model.pojo.CategoryMeal;
import com.ewida.mealmaster.model.pojo.Meal;
import com.ewida.mealmaster.utils.Constants;

import java.util.List;

public class HomeFragment extends Fragment implements HomeViewContract{

    private FragmentHomeBinding binding;
    private HomePresenterContract presenter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater,container,false);
        presenter = new HomePresenter(this);
        presenter.getRandomMeal();
        presenter.getVegetarianMeals();
        presenter.getDesserts();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String username = getContext().getSharedPreferences(Constants.SharedPref.PREFS_FILE_NAME, Context.MODE_PRIVATE).getString(Constants.SharedPref.USER_NAME_KEY,"bla bla bla");
        binding.tvUsername.setText(username);
    }

    @Override
    public void showRandomMeal(Meal meal) {
        Glide.with(binding.ivRadnomMeal).load(meal.getStrMealThumb()).placeholder(R.drawable.image_placeholder).transition(DrawableTransitionOptions.withCrossFade()).into(binding.ivRadnomMeal);
        binding.setRandomMeal(meal);
        binding.randomMealCard.setVisibility(VISIBLE);
        binding.randomMealShimmer.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_out_anim));
        binding.randomMealCard.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_anim));
    }

    @Override
    public void showVegetarianMeals(List<CategoryMeal> meals) {
        HomeMealsAdapter adapter = new HomeMealsAdapter(meals,true);
        binding.rvVegetarianMeals.setAdapter(adapter);
        binding.rvVegetarianMeals.setVisibility(VISIBLE);
        binding.rvVegetarianMeals.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.fade_in_anim));
        binding.vegetarianFoodShimmer.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.fade_out_anim));
    }

    @Override
    public void showDesserts(List<CategoryMeal> desserts) {
        HomeMealsAdapter adapter = new HomeMealsAdapter(desserts,false);
        binding.rvDesserts.setAdapter(adapter);
        binding.rvDesserts.setVisibility(VISIBLE);
        binding.rvDesserts.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.fade_in_anim));
        binding.dessertsShimmer.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.fade_out_anim));
    }

    @Override
    public void showErrorMessage(String errorMSg) {

    }
}