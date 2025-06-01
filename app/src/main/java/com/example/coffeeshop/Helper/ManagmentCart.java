package com.example.coffeeshop.Helper;

import android.content.Context;
import android.widget.Toast;

import com.example.coffeeshop.Domain.ItemsModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ManagmentCart {
    private Context context;
    private DatabaseReference cartRef;
    private String userId;

    public ManagmentCart(Context context) {
        this.context = context;
        this.userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.cartRef = FirebaseDatabase.getInstance().getReference("carts").child(userId);
    }

    public void insertItems(ItemsModel item) {
        cartRef.child(item.getTitle()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Item exists, update quantity
                    ItemsModel existingItem = snapshot.getValue(ItemsModel.class);
                    existingItem.setNumberInCart(existingItem.getNumberInCart() + item.getNumberInCart());
                    cartRef.child(item.getTitle()).setValue(existingItem);
                } else {
                    // New item, add to cart
                    cartRef.child(item.getTitle()).setValue(item);
                }
                Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(context, "Failed to add to cart", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getListCart(final CartListListener listener) {
        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<ItemsModel> list = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ItemsModel item = snapshot.getValue(ItemsModel.class);
                    list.add(item);
                }
                listener.onCartLoaded(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError.toException());
            }
        });
    }

    public void clearCart() {
        cartRef.removeValue();
    }

    public void minusItem(ItemsModel item, ChangeNumberItemsListener listener) {
        if (item.getNumberInCart() == 1) {
            cartRef.child(item.getTitle()).removeValue();
        } else {
            item.setNumberInCart(item.getNumberInCart() - 1);
            cartRef.child(item.getTitle()).setValue(item);
        }
        listener.onChanged();
    }

    public void removeItem(ItemsModel item, ChangeNumberItemsListener listener) {
        cartRef.child(item.getTitle()).removeValue();
        listener.onChanged();
    }

    public void plusItem(ItemsModel item, ChangeNumberItemsListener listener) {
        item.setNumberInCart(item.getNumberInCart() + 1);
        cartRef.child(item.getTitle()).setValue(item);
        listener.onChanged();
    }

    public void getTotalFee(final TotalFeeListener listener) {
        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double fee = 0.0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ItemsModel item = snapshot.getValue(ItemsModel.class);
                    fee += item.getPrice() * item.getNumberInCart();
                }
                listener.onTotalCalculated(fee);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError.toException());
            }
        });
    }

    public interface CartListListener {
        void onCartLoaded(ArrayList<ItemsModel> cartList);
        void onError(Exception exception);
    }

    public interface TotalFeeListener {
        void onTotalCalculated(double total);
        void onError(Exception exception);
    }

    public interface ChangeNumberItemsListener {
        void onChanged();
    }
}