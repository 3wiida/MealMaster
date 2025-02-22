package com.ewida.mealmaster.explore_meals.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.ewida.mealmaster.R;
import com.ewida.mealmaster.data.model.CategoryMeal;
import com.ewida.mealmaster.databinding.ExploreMealLayoutBinding;
import java.util.List;

public class ExploreMealsAdapter extends RecyclerView.Adapter<ExploreMealsAdapter.ItemViewHolder> {

    private List<CategoryMeal> meals;
    private OnMealClickListener onMealClickListener;

    public ExploreMealsAdapter(List<CategoryMeal> meal, OnMealClickListener onMealClickListener) {
        this.meals = meal;
        this.onMealClickListener = onMealClickListener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ExploreMealLayoutBinding binding = ExploreMealLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
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
        private final ExploreMealLayoutBinding binding;

        public ItemViewHolder(ExploreMealLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(CategoryMeal meal) {
            Glide.with(binding.ivMeal).load(meal.getStrMealThumb()).placeholder(R.drawable.image_placeholder).transition(DrawableTransitionOptions.withCrossFade()).into(binding.ivMeal);
            binding.setMeal(meal);
            binding.getRoot().setOnClickListener(view -> onMealClickListener.onMealClicked(meal));
        }
    }

    public interface OnMealClickListener {
        void onMealClicked(CategoryMeal meal);
    }
}
