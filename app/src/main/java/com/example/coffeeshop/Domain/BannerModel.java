package com.example.coffeeshop.Domain;

public class BannerModel {
    private String url;

    // Default constructor (required for Firebase and other frameworks)
    public BannerModel() {
        this.url = "";
    }

    // Constructor with parameter
    public BannerModel(String url) {
        this.url = url;
    }

    // Getter
    public String getUrl() {
        return url;
    }

    // Setter
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "BannerModel{" +
                "url='" + url + '\'' +
                '}';
    }
}
