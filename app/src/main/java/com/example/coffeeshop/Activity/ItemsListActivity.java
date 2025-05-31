package com.example.coffeeshop.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.coffeeshop.Adapter.ItemsListCategoryAdapter;
import com.example.coffeeshop.Domain.ItemsModel;
import com.example.coffeeshop.View.MainViewModel;
import com.example.coffeeshop.databinding.ActivityItemsListBinding;

import java.util.List;

public class ItemsListActivity extends AppCompatActivity {

    private ActivityItemsListBinding binding;
    private final MainViewModel viewModel = new MainViewModel();

    private String id = "";
    private String title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemsListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getBundle();
        initList();
    }

    private void getBundle() {
        id = getIntent().getStringExtra("id");
        title = getIntent().getStringExtra("title");
        if (title != null) {
            binding.catagoryList.setText(title);
        }
    }

    private void initList() {
        binding.progressBar.setVisibility(View.VISIBLE);

        viewModel.loadItems(id).observe(this, new Observer<List<ItemsModel>>() {
            @Override
            public void onChanged(List<ItemsModel> itemsModels) {
                binding.listView.setLayoutManager(new LinearLayoutManager(
                        ItemsListActivity.this,
                        LinearLayoutManager.VERTICAL,
                        false
                ));
                binding.listView.setAdapter(new ItemsListCategoryAdapter(itemsModels));
                binding.progressBar.setVisibility(View.GONE);
            }
        });

        binding.backBtn.setOnClickListener(v -> finish());
    }
}
