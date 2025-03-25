package com.example.coffeeshop.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.coffeeshop.Adapter.CategoryAdapter;
import com.example.coffeeshop.Adapter.PopularAdapter;
import com.example.coffeeshop.View.MainViewModel;
import com.example.coffeeshop.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        initBanner();
        initCategory();
        initPopular();
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
