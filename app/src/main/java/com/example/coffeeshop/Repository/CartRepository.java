package com.example.coffeeshop.Repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.coffeeshop.Domain.ItemsModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartRepository {
    private static final String PREFS_NAME = "CoffeeShopPrefs";
    private static final String USER_ID_KEY = "uid";

    private final DatabaseReference cartRef;
    private final String userId;

    public CartRepository(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userId = prefs.getString(USER_ID_KEY, null);

        if (userId == null) {
            throw new IllegalStateException("User ID not found in SharedPreferences");
        }

        cartRef = FirebaseDatabase.getInstance().getReference("carts").child(userId);
    }

    public void insertOrUpdateItems(List<ItemsModel> items, final DatabaseOperationCallback callback) {
        Map<String, Object> cartUpdates = new HashMap<>();
        for (ItemsModel item : items) {
            cartUpdates.put(item.getTitle(), item);
        }

        cartRef.updateChildren(cartUpdates)
                .addOnSuccessListener(aVoid -> {
                    if (callback != null) callback.onSuccess();
                })
                .addOnFailureListener(e -> {
                    if (callback != null) callback.onFailure(e);
                });
    }

    public void getCartItems(final CartDataCallback callback) {
        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<ItemsModel> items = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ItemsModel item = snapshot.getValue(ItemsModel.class);
                    if (item != null) {
                        items.add(item);
                    }
                }
                callback.onCartDataLoaded(items);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError.toException());
            }
        });
    }

    public void removeItem(String itemTitle, final DatabaseOperationCallback callback) {
        cartRef.child(itemTitle).removeValue()
                .addOnSuccessListener(aVoid -> {
                    if (callback != null) callback.onSuccess();
                })
                .addOnFailureListener(e -> {
                    if (callback != null) callback.onFailure(e);
                });
    }

    public void clearCart(final DatabaseOperationCallback callback) {
        cartRef.removeValue()
                .addOnSuccessListener(aVoid -> {
                    if (callback != null) callback.onSuccess();
                })
                .addOnFailureListener(e -> {
                    if (callback != null) callback.onFailure(e);
                });
    }

    public interface CartDataCallback {
        void onCartDataLoaded(List<ItemsModel> items);
        void onError(Exception e);
    }

    public interface DatabaseOperationCallback {
        void onSuccess();
        void onFailure(Exception e);
    }
}