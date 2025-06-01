package com.example.coffeeshop.Activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.coffeeshop.Adapter.CartAdapter;
import com.example.coffeeshop.Helper.ChangeNumberItemsListener;
import com.example.coffeeshop.Helper.ManagmentCart;
import com.example.coffeeshop.databinding.ActivityCartBinding;


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
        binding.cartView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.cartView.setAdapter(new CartAdapter(managmentCart.getListCart(), this, new ChangeNumberItemsListener() {
            @Override
            public void onChanged() {
                calculateCart();
            }
        }));
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());
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
