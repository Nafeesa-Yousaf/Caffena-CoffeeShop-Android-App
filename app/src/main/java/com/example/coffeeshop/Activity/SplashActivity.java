package com.example.coffeeshop.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coffeeshop.R;
import com.example.coffeeshop.Repository.AuthRepository;
import com.example.coffeeshop.databinding.ActivitySplashBinding;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;
    private AuthRepository authRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySplashBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        // Change status bar color
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.darkBrown));
        authRepo = new AuthRepository(this);
        authRepo.checkCurrentUser(new AuthRepository.EmailSignInCallback() {
            @Override
            public void onSignInSuccess(FirebaseUser user) {
                goToMainActivity();
            }

            @Override
            public void onSignInFailure(String errorMessage) {
                showToast(errorMessage);
            }
        });
        binding.startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // This closes the current SplashActivity
            }
        });

    }
    private void goToMainActivity() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show());
    }
}