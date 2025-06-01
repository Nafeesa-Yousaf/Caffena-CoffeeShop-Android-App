package com.example.coffeeshop.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.coffeeshop.Domain.ItemsModel;
import com.example.coffeeshop.Helper.ManagmentCart;
import com.example.coffeeshop.R;
import com.example.coffeeshop.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;
    private ItemsModel item;
    private ManagmentCart managmentCart;
    private double basePrice = 0.0;
    private String selectedSize;
    private int quantity=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCart = new ManagmentCart(this);

        receiveBundleData();
        initSizeList();
        setupListeners();
    }

    private void receiveBundleData() {
        // Getting the item object passed via Intent
        if (getIntent() != null && getIntent().hasExtra("object")) {
            item = (ItemsModel) getIntent().getSerializableExtra("object");

            if (item != null) {
                // Load image with Glide
                Glide.with(this)
                        .load(item.getPicUrl().get(0))  // Assuming picurl is a List<String>
                        .into(binding.picMain);

                binding.titleTxt.setText(item.getTitle());
                binding.descriptionTxt.setText(item.getDescription());
                binding.ratingTxt.setText(String.valueOf(item.getRating()));

                binding.numberItemTxt.setText(String.valueOf(item.getNumberInCart()));
                basePrice = 1;
                selectedSize = "Small";
                binding.smallBtn.setBackgroundResource(R.drawable.stroke_brown_bg);
                updatePrice();

            }
        }
    }

    private void initSizeList() {
        binding.smallBtn.setOnClickListener(v -> {
            binding.smallBtn.setBackgroundResource(R.drawable.stroke_brown_bg);
            binding.mediumBtn.setBackgroundResource(0);
            binding.largeBtn.setBackgroundResource(0);
            selectedSize = "Small";
            updatePrice();
        });

        binding.mediumBtn.setOnClickListener(v -> {
            binding.smallBtn.setBackgroundResource(0);
            binding.mediumBtn.setBackgroundResource(R.drawable.stroke_brown_bg);
            binding.largeBtn.setBackgroundResource(0);
            selectedSize = "Medium";
            updatePrice();
        });

        binding.largeBtn.setOnClickListener(v -> {
            binding.smallBtn.setBackgroundResource(0);
            binding.mediumBtn.setBackgroundResource(0);
            binding.largeBtn.setBackgroundResource(R.drawable.stroke_brown_bg);
            selectedSize = "Large";
            updatePrice();
        });
    }

    private void setupListeners() {
        // Add to Cart button
        binding.addToCartBtn.setOnClickListener(v -> {
            int quantity = Integer.parseInt(binding.numberItemTxt.getText().toString());
            item.setNumberInCart(quantity);
            managmentCart.insertItems(item);
        });

        // Back button
        binding.backBtn.setOnClickListener(v -> {

            finish();
        });

        // Plus button to increase quantity
        binding.plusCart.setOnClickListener(v -> {
             quantity++;
            item.setNumberInCart(quantity);
            binding.numberItemTxt.setText(String.valueOf(quantity));
            updatePrice();
        });

        // Minus button to decrease quantity
        binding.minusBtn.setOnClickListener(v -> {
            if (quantity > 0) {
                quantity--;
                item.setNumberInCart(quantity);
                binding.numberItemTxt.setText(String.valueOf(quantity));
                updatePrice();
            }
        });
    }

    private void updatePrice() {
        double sizeExtra = 0;

        if (selectedSize.equals("Medium")) {
            sizeExtra = 5.0;
        } else if (selectedSize.equals("Large")) {
            sizeExtra = 10.0;
        }

        double totalPrice = (basePrice + sizeExtra) * quantity;
        binding.priceTxt.setText("$" + String.format("%.2f", totalPrice));
    }

}
