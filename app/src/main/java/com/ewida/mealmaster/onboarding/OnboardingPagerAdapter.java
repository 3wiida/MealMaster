package com.ewida.mealmaster.onboarding;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ewida.mealmaster.databinding.OnboardingItemLayoutBinding;
import com.ewida.mealmaster.data.model.OnboardingItem;
import java.util.List;

public class OnboardingPagerAdapter extends RecyclerView.Adapter<OnboardingPagerAdapter.ItemViewHolder> {

    private final List<OnboardingItem> items;

    public OnboardingPagerAdapter(List<OnboardingItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OnboardingItemLayoutBinding binding = OnboardingItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static final class ItemViewHolder extends RecyclerView.ViewHolder {
        private final OnboardingItemLayoutBinding binding;

        ItemViewHolder(OnboardingItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(OnboardingItem item) {
            binding.setItem(item);
        }
    }
}
