package com.ewida.mealmaster.main.explore.view;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.ewida.mealmaster.data.model.ExploreItem;
import com.ewida.mealmaster.databinding.ExploreItemLayoutBinding;
import java.util.List;

public class ExploreItemAdapter extends RecyclerView.Adapter<ExploreItemAdapter.ItemViewHolder> {

    private final List<ExploreItem> items;
    private OnExploreItemClickListener onExploreItemClickListener;

    public ExploreItemAdapter(List<ExploreItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ExploreItemLayoutBinding binding = ExploreItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
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

    public void setOnItemClick(OnExploreItemClickListener onExploreItemClickListener){
        this.onExploreItemClickListener = onExploreItemClickListener;
    }

    public final class ItemViewHolder extends RecyclerView.ViewHolder{
        private final ExploreItemLayoutBinding binding;

        public ItemViewHolder(ExploreItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ExploreItem item){
            binding.setItem(item);
            binding.getRoot().setOnClickListener(view->{
                onExploreItemClickListener.onItemClicked(item);
            });
            Glide.with(binding.ivItemThumb)
                    .load(item.getThumbnail())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(binding.ivItemThumb);
        }
    }

    public interface OnExploreItemClickListener{
        void onItemClicked(ExploreItem item);
    }
}
