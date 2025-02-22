package com.ewida.mealmaster.main.explore.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.ewida.mealmaster.data.model.ExploreItem;
import com.ewida.mealmaster.databinding.ExploreItemLayoutBinding;
import com.ewida.mealmaster.databinding.ExploreSearchItemLayoutBinding;
import com.ewida.mealmaster.databinding.SearchResultItemBinding;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ItemViewHolder> {

    private List<ExploreItem> items = new ArrayList<>();

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ExploreSearchItemLayoutBinding binding = ExploreSearchItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
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

    public void setList(List<ExploreItem> items){
        this.items = items;
        notifyDataSetChanged();
    }

    public final class ItemViewHolder extends RecyclerView.ViewHolder{
        private final ExploreSearchItemLayoutBinding binding;

        public ItemViewHolder(ExploreSearchItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ExploreItem item){
            binding.setItem(item);
            Glide.with(binding.ivItemThumb)
                    .load(item.getThumbnail())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(binding.ivItemThumb);
        }
    }
}
