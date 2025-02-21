package com.ewida.mealmaster.search.view;

import android.annotation.SuppressLint;
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
import com.ewida.mealmaster.databinding.SearchResultItemBinding;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ItemViewHolder> {

    private List<Meal> results;
    private OnMealClickListener onMealClickListener;

    public SearchAdapter(List<Meal> results, OnMealClickListener onMealClickListener) {
        this.results = results;
        this.onMealClickListener = onMealClickListener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SearchResultItemBinding binding = SearchResultItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(results.get(position));
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void setList(List<Meal> results) {
        this.results = results;
        notifyDataSetChanged();
    }

    public final class ItemViewHolder extends RecyclerView.ViewHolder {
        private final SearchResultItemBinding binding;

        public ItemViewHolder(SearchResultItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Meal meal) {
            Glide.with(binding.ivMeal)
                    .load(meal.getStrMealThumb())
                    .placeholder(R.drawable.image_placeholder)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(binding.ivMeal);
            binding.setMeal(meal);
            binding.getRoot().setOnClickListener(view -> onMealClickListener.onMealClicked(meal));
        }
    }

    public interface OnMealClickListener {
        void onMealClicked(Meal meal);
    }
}
