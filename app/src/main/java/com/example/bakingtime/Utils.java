package com.example.bakingtime;

import android.util.Log;

import com.example.bakingtime.database.Ingredient;

import java.util.List;

public class Utils {

    public static String getIngredientsList(List<Ingredient> ingredients) {
        if (ingredients == null) {
            return "";
        }

        StringBuilder ingredientsList = new StringBuilder();
        for (int i = 0; i < ingredients.size(); i++) {
            Ingredient ingredient = ingredients.get(i);
            ingredientsList.append("* ")
                    .append(getQuantityInString(ingredient.getQuantity()))
                    .append(" ")
                    .append(ingredient.getMeasure())
                    .append(" ")
                    .append(ingredient.getIngredient())
                    .append("\n\n");

        }
        return ingredientsList.toString();
    }

    private static String getQuantityInString(double quantityDouble) {
        int quantityInteger = (int)quantityDouble;
        return quantityDouble == Math.floor(quantityDouble) ?
                String.valueOf(quantityInteger) : String.valueOf(quantityDouble);
    }
}
