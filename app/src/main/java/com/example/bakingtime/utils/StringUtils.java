/*
    Copyright (C) 2020 The Android Open Source Project

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

package com.example.bakingtime.utils;

import com.example.bakingtime.database.Ingredient;

import java.util.List;

public class StringUtils {

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
        int quantityInteger = (int) quantityDouble;
        return quantityDouble == Math.floor(quantityDouble) ?
                String.valueOf(quantityInteger) : String.valueOf(quantityDouble);
    }
}
