package com.example.bakingtime.database;

public class Recipe {

    private int id;
    private String name;
//    private Ingredient[] ingredients;
//    private Step[] steps;
//    private int servings;

//    public Recipe(int id, String name, Ingredient[] ingredients, Step[] steps, int servings) {
    public Recipe(int id, String name) {
        this.id = id;
        this.name = name;
//        this.ingredients = ingredients;
//        this.steps = steps;
//        this.servings = servings;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*
    public Ingredient[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(Ingredient[] ingredients) {
        this.ingredients = ingredients;
    }

    public Step[] getSteps() {
        return steps;
    }

    public void setSteps(Step[] steps) {
        this.steps = steps;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }
     */
}
