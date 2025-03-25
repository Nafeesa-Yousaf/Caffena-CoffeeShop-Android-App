package com.example.coffeeshop.Domain;

public class CategoryModel {
    private String title;
    private int id;

    // Constructor
    public CategoryModel(String title, int id) {
        this.title = title;
        this.id = id;
    }

    // Default Constructor
    public CategoryModel() {
        this.title = "";
        this.id = 0;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "CategoryModel{" +
                "title='" + title + '\'' +
                ", id=" + id +
                '}';
    }
}

