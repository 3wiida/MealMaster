package com.ewida.mealmaster.main.plans.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.ewida.mealmaster.R;
import com.ewida.mealmaster.data.model.Meal;
import com.ewida.mealmaster.databinding.PlanedItemLayoutBinding;


public class PlannedMealsAdapter extends ListAdapter<Meal, PlannedMealsAdapter.ItemViewHolder> {
    private final OnMealClickListener onMealClickListener;

    protected PlannedMealsAdapter(OnMealClickListener onMealClickListener) {
        super(DIFF_CALLBACK);
        this.onMealClickListener = onMealClickListener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PlanedItemLayoutBinding binding = PlanedItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public final class ItemViewHolder extends RecyclerView.ViewHolder {
        private final PlanedItemLayoutBinding binding;

        public ItemViewHolder(PlanedItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Meal meal) {
            Glide.with(binding.ivMealThumbnail)
                    .load(meal.getStrMealThumb())
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.image_placeholder)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(binding.ivMealThumbnail);
            binding.tvMealTitle.setText(meal.getStrMeal());
            binding.getRoot().setOnClickListener(view->{
                onMealClickListener.onMealClicked(meal);
            });
            binding.icDelete.setOnClickListener(view->{
                onMealClickListener.onRemoveClicked(meal);
            });
        }
    }

    public static final DiffUtil.ItemCallback<Meal> DIFF_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull Meal oldMeal, @NonNull Meal newMeal) {
            return oldMeal.equals(newMeal);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Meal oldMeal, @NonNull Meal newMeal) {
            return oldMeal.getIdMeal().equals(newMeal.getIdMeal());

        }
    };

    protected interface OnMealClickListener{
        void onMealClicked(Meal meal);
        void onRemoveClicked(Meal meal);
    }
}
