package com.ewida.mealmaster.main.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.ewida.mealmaster.R;
import com.ewida.mealmaster.databinding.HomeMealItemBinding;
import com.ewida.mealmaster.data.model.CategoryMeal;

import java.util.List;

public class HomeMealsAdapter extends RecyclerView.Adapter<HomeMealsAdapter.ItemViewHolder> {

    private final List<CategoryMeal> meals;
    private final boolean isVegetarian;
    private final OnMealClickListener onMealClickListener;

    public HomeMealsAdapter(List<CategoryMeal> meals, boolean isVegetarian, OnMealClickListener onMealClickListener) {
        this.meals = meals;
        this.isVegetarian = isVegetarian;
        this.onMealClickListener = onMealClickListener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HomeMealItemBinding binding = HomeMealItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(meals.get(position));
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public final class ItemViewHolder extends RecyclerView.ViewHolder {
        private final HomeMealItemBinding binding;

        public ItemViewHolder(HomeMealItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(CategoryMeal meal) {
            Glide.with(binding.ivMeal)
                    .load(meal.getStrMealThumb())
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.image_placeholder)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(binding.ivMeal);
            binding.setMeal(meal);
            binding.iconMeal.setImageResource(isVegetarian ? R.drawable.ic_vegetarian : R.drawable.ic_dessert);
            binding.getRoot().setOnClickListener(view -> {
                onMealClickListener.onMealClicked(meal);
            });
        }
    }

    public interface OnMealClickListener {
        void onMealClicked(CategoryMeal meal);
    }

}
