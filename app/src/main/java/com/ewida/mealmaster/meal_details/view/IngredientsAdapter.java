package com.ewida.mealmaster.meal_details.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.ewida.mealmaster.R;
import com.ewida.mealmaster.data.model.Ingredient;
import com.ewida.mealmaster.databinding.IngredientItemLayoutBinding;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ItemViewHolder> {

    private final List<Ingredient> ingredients;

    public IngredientsAdapter(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        IngredientItemLayoutBinding binding = IngredientItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(ingredients.get(position));
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public static final class ItemViewHolder extends RecyclerView.ViewHolder {
        private final IngredientItemLayoutBinding binding;

        public ItemViewHolder(IngredientItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Ingredient ingredient) {
            binding.setIngredient(ingredient);
            Glide.with(binding.ivIngredient)
                    .load(ingredient.getThumbnail())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(binding.ivIngredient);
        }
    }
}
