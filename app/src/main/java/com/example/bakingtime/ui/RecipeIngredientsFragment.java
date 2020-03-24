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

package com.example.bakingtime.ui;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bakingtime.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class RecipeIngredientsFragment extends Fragment {

    private static final String SAVED_INSTANCE_FROM_WIDGET = "from widget";
    private boolean mFromWidget;

    public RecipeIngredientsFragment() {
    }

    RecipeIngredientsFragment(boolean fromWidget) {
        mFromWidget = fromWidget;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mFromWidget = savedInstanceState.getBoolean(SAVED_INSTANCE_FROM_WIDGET);
        }

        String ingredients;
        if (mFromWidget) {
            ingredients = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(
                    getString(R.string.pref_recipe_ingredients_list_key), getString(R.string.pref_default_recipe_ingredients_list));
        } else {
            ingredients = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(
                    getString(R.string.pref_recipe_ingredients_list_key), null);
        }

        if (ingredients == null || ingredients.isEmpty()) {
            ((RecipeIngredientsActivity) requireActivity()).closeOnError();
            return null;
        }

        View rootView = inflater.inflate(R.layout.fragment_recipe_ingredients, container, false);
        ((TextView) rootView.findViewById(R.id.recipe_ingredients_list_tv)).setText(ingredients);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_INSTANCE_FROM_WIDGET, mFromWidget);
    }
}
