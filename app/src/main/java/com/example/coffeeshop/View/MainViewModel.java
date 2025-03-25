package com.example.coffeeshop.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;


import com.example.coffeeshop.Domain.BannerModel;
import com.example.coffeeshop.Domain.CategoryModel;
import com.example.coffeeshop.Domain.ItemsModel;
import com.example.coffeeshop.Repository.MainRepository;

import java.util.List;

public class MainViewModel extends ViewModel {

    private final MainRepository repository;

    public MainViewModel() {
        this.repository = new MainRepository();
    }

    public LiveData<List<BannerModel>> loadBanner() {
        return repository.loadBanner();
    }

    public LiveData<List<CategoryModel>> loadCategory() {
        return repository.loadCategory();
    }

    public LiveData<List<ItemsModel>> loadPopular() {
        return repository.loadPopular();
    }
}
