package com.example.coffeeshop.Domain;

public class CategoryModel {
    private String title;
    private int id;

    public CategoryModel(String title, int id) {
        this.title = title;
        this.id = id;
    }

    public CategoryModel() {
        this.title = "";
        this.id = 0;
    }

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
        return "CategoryModel{" + "title='" + title + '\'' + ", id=" + id + '}';
    }
}

