package com.example.bakingtime.database;

public class Recipe {

    private String id;

    public Recipe(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
