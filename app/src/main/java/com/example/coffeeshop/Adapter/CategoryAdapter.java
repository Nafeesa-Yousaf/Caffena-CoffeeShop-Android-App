package com.example.coffeeshop.Adapter;


import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeshop.Activity.ItemsListActivity;
import com.example.coffeeshop.Domain.CategoryModel;
import com.example.coffeeshop.R;
import com.example.coffeeshop.databinding.ViewholderCategoryBinding;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<CategoryModel> items;
    private Context context;
    private int lastSelectedPosition = -1;
    private int selectedPosition = -1;

    public CategoryAdapter(List<CategoryModel> items) {
        this.items = items;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ViewholderCategoryBinding binding;

        public ViewHolder(ViewholderCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderCategoryBinding binding = ViewholderCategoryBinding.inflate(
                LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryModel item = items.get(position);
        holder.binding.titleCat.setText(item.getTitle());

        holder.binding.getRoot().setOnClickListener(v -> {
            lastSelectedPosition = selectedPosition;
            selectedPosition = position;

            notifyItemChanged(lastSelectedPosition);
            notifyItemChanged(selectedPosition);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Intent intent = new Intent(context, ItemsListActivity.class);
                intent.putExtra("id", String.valueOf(item.getId()));
                intent.putExtra("title", item.getTitle());

                ContextCompat.startActivity(context, intent, null);
            }, 500);


            if (selectedPosition == position) {
                holder.binding.titleCat.setBackgroundResource(R.drawable.drak_brown_bg);
                holder.binding.titleCat.setTextColor(context.getResources().getColor(R.color.white));
            } else {
                holder.binding.titleCat.setBackgroundResource(R.drawable.white_bg);
                holder.binding.titleCat.setTextColor(context.getResources().getColor(R.color.darkBrown));
            }
        });

        if (selectedPosition == position) {
            holder.binding.titleCat.setBackgroundResource(R.drawable.drak_brown_bg);
            holder.binding.titleCat.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            holder.binding.titleCat.setBackgroundResource(R.drawable.white_bg);
            holder.binding.titleCat.setTextColor(context.getResources().getColor(R.color.darkBrown));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

