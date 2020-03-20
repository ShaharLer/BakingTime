package com.example.bakingtime;

import com.example.bakingtime.database.Ingredient;

import java.util.List;

public class Utils {

    private static final String INGREDIENT_PREFIX = "*  ";
    private static final String INGREDIENT_SEPARATOR = " ";
    private static final String INGREDIENTS_SEPARATOR = "\n\n";

    public static String getIngredientsList(List<Ingredient> ingredients) {
        if (ingredients == null) {
            return null;
        }

        StringBuilder ingredientsList = new StringBuilder();
        for (int i = 0; i < ingredients.size(); i++) {
            Ingredient ingredient = ingredients.get(i);
            ingredientsList.append(INGREDIENT_PREFIX)
                    .append(getQuantityInString(ingredient.getQuantity()))
                    .append(INGREDIENT_SEPARATOR)
                    .append(ingredient.getMeasure())
                    .append(INGREDIENT_SEPARATOR)
                    .append(ingredient.getIngredient());

            if (i < ingredients.size() - 1) {
                ingredientsList.append(INGREDIENTS_SEPARATOR);
            }
        }
        return ingredientsList.toString();
    }

    private static String getQuantityInString(double quantityDouble) {
        int quantityInteger = (int)quantityDouble;
        return quantityDouble == Math.floor(quantityDouble) ?
                String.valueOf(quantityInteger) : String.valueOf(quantityDouble);
    }
}
