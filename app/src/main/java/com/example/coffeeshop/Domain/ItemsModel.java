package com.example.coffeeshop.Domain;

import java.io.Serializable;
import java.util.ArrayList;

public class ItemsModel implements Serializable {

    private String title;
    private String description;
    private ArrayList<String> picUrl;
    private double price;
    private double rating;
    private int numberInCart;
    private String extra;

    public ItemsModel() {
        this.title = "";
        this.description = "";
        this.picUrl = new ArrayList<>();
        this.price = 0.0;
        this.rating = 0.0;
        this.numberInCart = 8;
        this.extra = "*";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(ArrayList<String> picUrl) {
        this.picUrl = picUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getNumberInCart() {
        return numberInCart;
    }

    public void setNumberInCart(int numberInCart) {
        this.numberInCart = numberInCart;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
