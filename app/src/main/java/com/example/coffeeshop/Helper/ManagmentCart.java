package com.example.coffeeshop.Helper;

import android.content.Context;
import android.widget.Toast;

import com.example.coffeeshop.Domain.ItemsModel;

import java.util.ArrayList;

public class ManagmentCart {

    private Context context;
    private TinyDB tinyDB;

    public ManagmentCart(Context context) {
        this.context = context;
        this.tinyDB = new TinyDB(context);
    }

    public void insertItems(ItemsModel item) {
        ArrayList<ItemsModel> listItem = getListCart();
        boolean existAlready = false;
        int index = -1;

        for (int i = 0; i < listItem.size(); i++) {
            if (listItem.get(i).getTitle().equals(item.getTitle())) {
                existAlready = true;
                index = i;
                break;
            }
        }

        if (existAlready) {
            listItem.get(index).setNumberInCart(item.getNumberInCart());
        } else {
            listItem.add(item);
        }

        tinyDB.putListObject("CartList", listItem);
        Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<ItemsModel> getListCart() {
        ArrayList<ItemsModel> list = tinyDB.getListObject("CartList");
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    public void minusItem(ArrayList<ItemsModel> listItems, int position, ChangeNumberItemsListener listener) {
        if (listItems.get(position).getNumberInCart() == 1) {
            listItems.remove(position);
        } else {
            listItems.get(position).setNumberInCart(listItems.get(position).getNumberInCart() - 1);
        }
        tinyDB.putListObject("CartList", listItems);
        listener.onChanged();
    }

    public void removeItem(ArrayList<ItemsModel> listItems, int position, ChangeNumberItemsListener listener) {
        listItems.remove(position);
        tinyDB.putListObject("CartList", listItems);
        listener.onChanged();
    }

    public void plusItem(ArrayList<ItemsModel> listItems, int position, ChangeNumberItemsListener listener) {
        listItems.get(position).setNumberInCart(listItems.get(position).getNumberInCart() + 1);
        tinyDB.putListObject("CartList", listItems);
        listener.onChanged();
    }

    public double getTotalFee() {
        ArrayList<ItemsModel> listItem = getListCart();
        double fee = 0.0;
        for (ItemsModel item : listItem) {
            fee += item.getPrice() * item.getNumberInCart();
        }
        return fee;
    }
}
