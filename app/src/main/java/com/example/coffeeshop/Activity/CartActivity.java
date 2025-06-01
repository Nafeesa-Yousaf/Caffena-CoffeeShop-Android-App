package com.example.coffeeshop.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.coffeeshop.Adapter.CartAdapter;
import com.example.coffeeshop.Domain.ItemsModel;
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

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private ActivityCartBinding binding;
    private ManagmentCart managmentCart;
    private double tax = 0.0;
    private CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCart = new ManagmentCart(this);

        setVariable();
        initCartList();
    }

    private void initCartList() {
        managmentCart.getListCart(new ManagmentCart.CartListListener() {
            @Override
            public void onCartLoaded(ArrayList<ItemsModel> cartList) {
                runOnUiThread(() -> {
                    if (cartList.isEmpty()) {
                        binding.emptyTxt.setVisibility(View.VISIBLE);
                        binding.cartView.setVisibility(View.GONE);
                    } else {
                        binding.emptyTxt.setVisibility(View.GONE);
                        binding.cartView.setVisibility(View.VISIBLE);

                        cartAdapter = new CartAdapter(cartList, CartActivity.this, new ChangeNumberItemsListener() {
                            @Override
                            public void onChanged() {
                                calculateCart();
                            }
                        });

                        binding.cartView.setLayoutManager(new LinearLayoutManager(CartActivity.this, LinearLayoutManager.VERTICAL, false));
                        binding.cartView.setAdapter(cartAdapter);
                    }
                    calculateCart();
                });
            }

            @Override
            public void onError(Exception exception) {
                runOnUiThread(() -> {
                    Toast.makeText(CartActivity.this, "Error loading cart: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());

        binding.checkoutBtn.setOnClickListener(v -> {
            managmentCart.getListCart(new ManagmentCart.CartListListener() {
                @Override
                public void onCartLoaded(ArrayList<ItemsModel> cartList) {
                    runOnUiThread(() -> {
                        if (cartList.isEmpty()) {
                            Toast.makeText(CartActivity.this, "Your cart is empty", Toast.LENGTH_SHORT).show();
                        } else {
                            showCheckoutDialog();
                        }
                    });
                }

                @Override
                public void onError(Exception exception) {
                    runOnUiThread(() -> {
                        Toast.makeText(CartActivity.this, "Error checking cart: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            });
        });
    }

    private void showCheckoutDialog() {
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

            managmentCart.clearCart();
            Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_LONG).show();

            initCartList();
        });

        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void calculateCart() {
        managmentCart.getTotalFee(new ManagmentCart.TotalFeeListener() {
            @Override
            public void onTotalCalculated(double totalFee) {
                runOnUiThread(() -> {
                    double percentTax = 0.02;
                    double delivery = 15;

                    tax = Math.round((totalFee * percentTax) * 100) / 100.0;
                    double total = Math.round((totalFee + tax + delivery) * 100) / 100.0;
                    double itemTotal = Math.round(totalFee * 100) / 100.0;

                    binding.totalFeeTxt.setText("$" + itemTotal);
                    binding.taxTxt.setText("$" + tax);
                    binding.deliveryTxt.setText("$" + delivery);
                    binding.totalTxt.setText("$" + total);
                });
            }

            @Override
            public void onError(Exception exception) {
                runOnUiThread(() -> {
                    Toast.makeText(CartActivity.this, "Error calculating total: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}