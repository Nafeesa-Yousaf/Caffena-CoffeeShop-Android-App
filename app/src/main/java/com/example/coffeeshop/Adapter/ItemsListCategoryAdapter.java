package com.example.coffeeshop.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.coffeeshop.Activity.DetailActivity;
import com.example.coffeeshop.Domain.ItemsModel;
import com.example.coffeeshop.databinding.ViewholderItemPicLeftBinding;
import com.example.coffeeshop.databinding.ViewholderItemPicRightBinding;

import java.util.List;

public class ItemsListCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<ItemsModel> items;
    private Context context;

    public static final int TYPE_ITEM1 = 0;
    public static final int TYPE_ITEM2 = 1;

    public ItemsListCategoryAdapter(List<ItemsModel> items) {
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 == 0 ? TYPE_ITEM1 : TYPE_ITEM2;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        if (viewType == TYPE_ITEM1) {
            ViewholderItemPicRightBinding binding = ViewholderItemPicRightBinding.inflate(LayoutInflater.from(context), parent, false);
            return new ViewHolderItem1(binding);
        } else {
            ViewholderItemPicLeftBinding binding = ViewholderItemPicLeftBinding.inflate(LayoutInflater.from(context), parent, false);
            return new ViewHolderItem2(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemsModel item = items.get(position);

        String picUrl = (item.getPicUrl() != null && !item.getPicUrl().isEmpty()) ? item.getPicUrl().get(0) : "";

        bindCommonData(holder, item.getTitle(), String.valueOf(item.getPrice()), (float) item.getRating(), picUrl, position);
    }

    private void bindCommonData(RecyclerView.ViewHolder holder, String titleTxt, String priceTxt, float rating, String picUrl, int position) {
        if (holder instanceof ViewHolderItem1) {
            ViewholderItemPicRightBinding binding = ((ViewHolderItem1) holder).binding;
            binding.titleTxt.setText(titleTxt);
            binding.priceTxt.setText(priceTxt);
            binding.ratingBar.setRating(rating);

            Glide.with(context).load(picUrl).into(binding.picMain);

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("object", items.get(position));
                context.startActivity(intent);
            });

        } else if (holder instanceof ViewHolderItem2) {
            ViewholderItemPicLeftBinding binding = ((ViewHolderItem2) holder).binding;
            binding.titleTxt.setText(titleTxt);
            binding.priceTxt.setText(priceTxt);
            binding.ratingBar.setRating(rating);

            Glide.with(context).load(picUrl).into(binding.picMain);

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("object", items.get(position));
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolderItem1 extends RecyclerView.ViewHolder {
        ViewholderItemPicRightBinding binding;

        public ViewHolderItem1(ViewholderItemPicRightBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    static class ViewHolderItem2 extends RecyclerView.ViewHolder {
        ViewholderItemPicLeftBinding binding;

        public ViewHolderItem2(ViewholderItemPicLeftBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
