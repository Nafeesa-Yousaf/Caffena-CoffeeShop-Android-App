package com.example.coffeeshop.Activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.coffeeshop.Adapter.CartAdapter;
import com.example.coffeeshop.Helper.ChangeNumberItemsListener;
import com.example.coffeeshop.Helper.ManagmentCart;
import com.example.coffeeshop.R;
import com.example.coffeeshop.databinding.ActivityCartBinding;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CartActivity extends AppCompatActivity {

    private ActivityCartBinding binding;
    private ManagmentCart managmentCart;
    private double tax = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCart = new ManagmentCart(this);

        calculateCart();
        setVariable();
        initCartList();
    }

    private void initCartList() {
        if (managmentCart.getListCart().isEmpty()) {
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.cartView.setVisibility(View.GONE);
        } else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.cartView.setVisibility(View.VISIBLE);

            binding.cartView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            binding.cartView.setAdapter(new CartAdapter(managmentCart.getListCart(), this, new ChangeNumberItemsListener() {
                @Override
                public void onChanged() {
                    calculateCart();
                }
            }));
        }
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());

        binding.checkoutBtn.setOnClickListener(v -> {
            // Check if cart is empty
            if (managmentCart.getListCart().isEmpty()) {
                Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show();
                return;
            }

            showCheckoutDialog();
        });
    }

    private void showCheckoutDialog() {
        // Inflate the dialog layout
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_checkout, null);
        EditText addressInput = dialogView.findViewById(R.id.address_input);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Order");
        builder.setView(dialogView);

        builder.setPositiveButton("Confirm", (dialog, which) -> {
            String address = addressInput.getText().toString().trim();
            if (address.isEmpty()) {
                Toast.makeText(this, "Please enter your address", Toast.LENGTH_SHORT).show();
                return;
            }

            // Process the order
           managmentCart.clearCart();
            Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_LONG).show();

            // Refresh the cart view
            initCartList();
            calculateCart();
        });

        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void calculateCart() {
        double percentTax = 0.02;
        double delivery = 15;

        double totalFee = managmentCart.getTotalFee();

        tax = Math.round((totalFee * percentTax) * 100) / 100.0;
        double total = Math.round((totalFee + tax + delivery) * 100) / 100.0;
        double itemTotal = Math.round(totalFee * 100) / 100.0;

        binding.totalFeeTxt.setText("$" + itemTotal);
        binding.taxTxt.setText("$" + tax);
        binding.deliveryTxt.setText("$" + delivery);
        binding.totalTxt.setText("$" + total);
    }
}