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
import com.google.firebase.database.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class CartRepository {
    private final DatabaseReference cartRef;

    public CartRepository() {
        this.cartRef = FirebaseDatabase.getInstance().getReference("carts");
    }

    // Add or update item in cart
    public CompletableFuture<Void> addToCart(String userId, String productId, Map<String, Object> productData) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        cartRef.child(userId).child(productId).setValue(productData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        future.complete(null);
                    } else {
                        future.completeExceptionally(task.getException());
                    }
                });

        return future;
    }

    // Remove item from cart
    public CompletableFuture<Void> removeFromCart(String userId, String productId) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        cartRef.child(userId).child(productId).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        future.complete(null);
                    } else {
                        future.completeExceptionally(task.getException());
                    }
                });

        return future;
    }

    // Clear entire cart
    public CompletableFuture<Void> clearCart(String userId) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        cartRef.child(userId).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        future.complete(null);
                    } else {
                        future.completeExceptionally(task.getException());
                    }
                });

        return future;
    }

    // Get cart items (single read)
    public CompletableFuture<Map<String, Map<String, Object>>> getCartItems(String userId) {
        CompletableFuture<Map<String, Map<String, Object>>> future = new CompletableFuture<>();

        cartRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Map<String, Object>> cartItems = new HashMap<>();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                        Map<String, Object> item = (Map<String, Object>) itemSnapshot.getValue();
                        cartItems.put(itemSnapshot.getKey(), item);
                    }
                }

                future.complete(cartItems);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });

        return future;
    }

    // Listen for real-time cart updates
    public void listenForCartChanges(String userId, CartUpdateListener listener) {
        cartRef.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Map<String, Object>> cartItems = new HashMap<>();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                        Map<String, Object> item = (Map<String, Object>) itemSnapshot.getValue();
                        cartItems.put(itemSnapshot.getKey(), item);
                    }
                }

                listener.onCartUpdated(cartItems);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError.toException());
            }
        });
    }

    public interface CartUpdateListener {
        void onCartUpdated(Map<String, Map<String, Object>> cartItems);
        void onError(Exception exception);
    }
}