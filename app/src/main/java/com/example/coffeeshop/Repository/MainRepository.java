package com.example.coffeeshop.Repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.coffeeshop.Domain.BannerModel;
import com.example.coffeeshop.Domain.CategoryModel;
import com.example.coffeeshop.Domain.ItemsModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainRepository {

    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    public LiveData<List<BannerModel>> loadBanner() {
        MutableLiveData<List<BannerModel>> listData = new MutableLiveData<>();
        DatabaseReference ref = firebaseDatabase.getReference("Banner");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<BannerModel> list = new ArrayList<>();

                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    BannerModel item = childSnapshot.getValue(BannerModel.class);
                    if (item != null) {
                        list.add(item);
                    }
                }
                System.out.println(list);
                listData.setValue(list);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle database error here (optional)
                Log.e("======>FirebaseError", "Data fetching error: " + error.getMessage());
            }
        });

        return listData;
    }

    public LiveData<List<CategoryModel>> loadCategory() {
        MutableLiveData<List<CategoryModel>> listData = new MutableLiveData<>();
        DatabaseReference ref = firebaseDatabase.getReference("Category");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<CategoryModel> list = new ArrayList<>();

                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    CategoryModel item = childSnapshot.getValue(CategoryModel.class);
                    if (item != null) {
                        list.add(item);
                    }
                }
                System.out.println(list);
                listData.setValue(list);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle database error here (optional)
                Log.e("======>FirebaseError", "Data fetching error: " + error.getMessage());
            }
        });

        return listData;
    }

    public LiveData<List<ItemsModel>> loadPopular() {
        MutableLiveData<List<ItemsModel>> listData = new MutableLiveData<>();
        DatabaseReference ref = firebaseDatabase.getReference("Popular");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<ItemsModel> list = new ArrayList<>();

                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    ItemsModel item = childSnapshot.getValue(ItemsModel.class);
                    if (item != null) {
                        list.add(item);
                    }
                }
                System.out.println(list);
                listData.setValue(list);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle database error here (optional)
                Log.e("======>FirebaseError", "Data fetching error: " + error.getMessage());
            }
        });

        return listData;
    }


    public LiveData<List<ItemsModel>> loadItemsCategory(String categoryId) {
        MutableLiveData<List<ItemsModel>> itemsLiveData = new MutableLiveData<>();
        DatabaseReference ref = firebaseDatabase.getReference("Items");

        ref.orderByChild("categoryId").equalTo(categoryId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<ItemsModel> list = new ArrayList<>();

                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    ItemsModel item = childSnapshot.getValue(ItemsModel.class);
                    if (item != null) {
                        list.add(item);
                    }
                }

                itemsLiveData.setValue(list);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("FirebaseError", "Data fetching error: " + error.getMessage());
            }
        });

        return itemsLiveData;
    }

}
