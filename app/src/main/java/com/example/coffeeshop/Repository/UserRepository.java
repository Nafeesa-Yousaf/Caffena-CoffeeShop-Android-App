package com.example.coffeeshop.Repository;


import androidx.annotation.NonNull;

import com.example.coffeeshop.Domain.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    // Fetch user data during login
    public void fetchUser(String uid, final OnUserFetchListener listener) {
        userRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel user = snapshot.getValue(UserModel.class);
                if (user != null) {
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

