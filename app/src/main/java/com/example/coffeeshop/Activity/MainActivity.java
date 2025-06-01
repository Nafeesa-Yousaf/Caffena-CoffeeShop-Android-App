package com.example.coffeeshop.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.coffeeshop.Adapter.CategoryAdapter;
import com.example.coffeeshop.Adapter.PopularAdapter;
import com.example.coffeeshop.R;
import com.example.coffeeshop.Repository.AuthRepository;
import com.example.coffeeshop.Repository.UserPreferences;
import com.example.coffeeshop.View.MainViewModel;
import com.example.coffeeshop.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    private AuthRepository authRepository;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authRepository = new AuthRepository(this);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authRepository.signOut();

                // Clear saved user data (if applicable)
                UserPreferences userPreferences = new UserPreferences(MainActivity.this);
                userPreferences.clearUser();  // Implement this method in your UserPreferences class

                // Redirect to LoginActivity
                Intent intent = new Intent(MainActivity.this, SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // clear back stack
                startActivity(intent);
                finish();
            }
        });


        // Fetch current user and display name
        displayUserName();

        initBanner();
        initCategory();
        initPopular();
        initBottomMenu();
    }

    private void initBottomMenu() {
        binding.cartBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        });
        binding.profileBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
    }

    private void displayUserName() {
        UserPreferences userPreferences = new UserPreferences(this);  // pass context
        String userName = userPreferences.getName();  // call on instance

        if (userName != null && !userName.isEmpty()) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Welcome, " + userName);
            }
        } else {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Welcome, Guest");
            }
        }
    }

    private void initCategory() {
        // Show progress bar
        binding.progressBarCatagory.setVisibility(View.VISIBLE);

        // Observe ViewModel data
        viewModel.loadCategory().observeForever(categories -> {
            // Set up RecyclerView layout manager
            binding.recyclerViewCat.setLayoutManager(
                    new LinearLayoutManager(
                            this, // Context
                            LinearLayoutManager.HORIZONTAL, // Orientation
                            false // reverseLayout
                    )
            );

            // Set adapter with category data
            binding.recyclerViewCat.setAdapter(new CategoryAdapter(categories));

            // Hide progress bar after loading data
            binding.progressBarCatagory.setVisibility(View.GONE);
        });
        viewModel.loadCategory();
    }

    private void initBanner() {
        binding.progressBarBanner.setVisibility(View.VISIBLE);

        viewModel.loadBanner().observe(this, banners -> {
            if (banners != null && !banners.isEmpty()) {
                Glide.with(MainActivity.this)
                        .load(banners.get(0).getUrl())
                        .into(binding.banner);
            }
            binding.progressBarBanner.setVisibility(View.GONE);
        });
    }



    private void initPopular() {
        binding.progressBarPopular.setVisibility(View.VISIBLE);

        viewModel.loadPopular().observeForever(items -> {
            binding.recyclerViewPopular.setLayoutManager(new GridLayoutManager(this, 2));
            binding.recyclerViewPopular.setAdapter(new PopularAdapter(items));

            binding.progressBarPopular.setVisibility(View.GONE);
        });

        viewModel.loadPopular();
    }

}
