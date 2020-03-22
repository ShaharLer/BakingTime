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

    private static final String SAVED_INSTANCE_INGREDINTS = "ingredients";

    private String mIngredients;
    private boolean mFromWidget;

    public RecipeIngredientsFragment() {
    }

    RecipeIngredientsFragment(boolean fromWidget) {
        mFromWidget = fromWidget;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(SAVED_INSTANCE_INGREDINTS)) {
            mIngredients = savedInstanceState.getString(SAVED_INSTANCE_INGREDINTS);
        } else {
            mIngredients = PreferenceManager.getDefaultSharedPreferences(getContext())
                    .getString(getString(R.string.pref_recipe_ingredients_list_key), getString(R.string.pref_default_recipe_ingredients_list));
        }

        if (mIngredients == null || mIngredients.isEmpty()) {
            if (mFromWidget) {
                ((MainActivity) requireActivity()).closeOnIngredientsListError();
            } else {
                ((RecipeIngredientsActivity) requireActivity()).closeOnError();
            }
            return null;
        }

        View rootView = inflater.inflate(R.layout.fragment_recipe_ingredients, container, false);
        ((TextView) rootView.findViewById(R.id.recipe_ingredients_list_tv)).setText(mIngredients);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(SAVED_INSTANCE_INGREDINTS, mIngredients);
    }
}
