package com.example.coffeeshop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.coffeeshop.Domain.ItemsModel;
import com.example.coffeeshop.Helper.ChangeNumberItemsListener;
import com.example.coffeeshop.Helper.ManagmentCart;
import com.example.coffeeshop.databinding.ViewholderCartBinding;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private ArrayList<ItemsModel> listItemSelected;
    private Context context;
    private ChangeNumberItemsListener changeNumberItemsListener;
    private ManagmentCart managmentCart;

    public CartAdapter(ArrayList<ItemsModel> listItemSelected, Context context, ChangeNumberItemsListener listener) {
        this.listItemSelected = listItemSelected;
        this.context = context;
        this.changeNumberItemsListener = listener;
        this.managmentCart = new ManagmentCart(context);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ViewholderCartBinding binding;

        public ViewHolder(ViewholderCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderCartBinding binding = ViewholderCartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        ItemsModel item = listItemSelected.get(position);

        holder.binding.titleTxt.setText(item.getTitle());
        holder.binding.feeEachItem.setText("$" + item.getPrice());
        holder.binding.totalEachItem.setText("$" + Math.round(item.getNumberInCart() * item.getPrice()));
        holder.binding.numberItemTxt.setText(String.valueOf(item.getNumberInCart()));

        Glide.with(holder.itemView.getContext())
                .load(item.getPicUrl().get(0))
                .apply(new RequestOptions().transform(new CenterCrop()))
                .into(holder.binding.picCart);

        holder.binding.plusEachItem.setOnClickListener(v -> {
            managmentCart.plusItem(listItemSelected, position, () -> {
                notifyDataSetChanged();
                if (changeNumberItemsListener != null) {
                    changeNumberItemsListener.onChanged();
                }
            });
        });

        holder.binding.minusEachItem.setOnClickListener(v -> {
            managmentCart.minusItem(listItemSelected, position, () -> {
                notifyDataSetChanged();
                if (changeNumberItemsListener != null) {
                    changeNumberItemsListener.onChanged();
                }
            });
        });

        holder.binding.removeItemBtn.setOnClickListener(v -> {
            managmentCart.removeItem(listItemSelected, position, () -> {
                notifyDataSetChanged();
                if (changeNumberItemsListener != null) {
                    changeNumberItemsListener.onChanged();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return listItemSelected.size();
    }
}
