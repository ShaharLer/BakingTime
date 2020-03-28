package com.example.bakingtime.utils;

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

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.bakingtime.R;
import com.example.bakingtime.database.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {

    private static final String INGREDIENT_SEPARATOR = " ";
    private static final int INGREDIENTS_LIST_DEFAULT_SIZE = 0;

    public static List<String> getIngredientsDetailsList(List<Ingredient> ingredientList) {
        if (ingredientList == null) {
            return null;
        }

        List<String> ingredientsDetailsList = new ArrayList<>();
        for (Ingredient ingredient : ingredientList) {
            ingredientsDetailsList.add(getIngredientAsString(ingredient));
        }

        return ingredientsDetailsList;
    }

    private static String getIngredientAsString(Ingredient ingredient) {
        return getQuantityInString(ingredient.getQuantity()) +
                INGREDIENT_SEPARATOR +
                ingredient.getMeasure() +
                INGREDIENT_SEPARATOR +
                ingredient.getIngredient();
    }

    private static String getQuantityInString(double quantityDouble) {
        int quantityInteger = (int) quantityDouble;
        return quantityDouble == Math.floor(quantityDouble) ?
                String.valueOf(quantityInteger) : String.valueOf(quantityDouble);
    }

    public static List<String> getSharedPreferencesIngredientsList(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        List<String> ingredientsDetailsList = new ArrayList<>();

        int ingredientsDetailsListSize = sharedPreferences.getInt(context.getString(
                R.string.pref_recipe_ingredients_list_size_key), INGREDIENTS_LIST_DEFAULT_SIZE);
        for (int i = 0; i < ingredientsDetailsListSize; i++) {
            String ingredientDetails = sharedPreferences.getString(
                    context.getString(R.string.pref_recipe_ingredients_list_name_key) + i, null);
            if (ingredientDetails == null) {
                return null;
            }
            ingredientsDetailsList.add(ingredientDetails);
        }

        return ingredientsDetailsList;
    }
}
