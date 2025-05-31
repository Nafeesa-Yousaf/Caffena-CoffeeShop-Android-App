package com.example.coffeeshop.Repository;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.coffeeshop.Domain.UserModel;
import com.example.coffeeshop.Repository.UserPreferences;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class UserRepository {

    private final DatabaseReference userRef;
    private final FirebaseAuth auth;

    public UserRepository() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        userRef = database.getReference("Users");
        auth = FirebaseAuth.getInstance();
    }

    // Register user in Realtime Database
    public void registerUser(String name, String email, String uid) {
        UserModel user = new UserModel(uid, name, email);
        userRef.child(uid).setValue(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // User registered successfully in DB
                    } else {
                        // Handle failure
                    }
                });
    }

    // Fetch user data during login and save to SharedPreferences
    public void fetchUser(String uid, Context context, final OnUserFetchListener listener) {
        userRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel user = snapshot.getValue(UserModel.class);
                if (user != null) {
                    // Save user data in SharedPreferences
                    UserPreferences preferences = new UserPreferences(context);
                    preferences.saveUser(user.getUid(), user.getName(), user.getEmail());

                    listener.onUserFetched(user);
                } else {
                    listener.onFailure("User not found in database");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onFailure(error.getMessage());
            }
        });
    }

    // Callback Interface
    public interface OnUserFetchListener {
        void onUserFetched(UserModel user);
        void onFailure(String errorMessage);
    }
}
