package com.example.bakingtime.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bakingtime.R;
import com.example.bakingtime.Utils;
import com.example.bakingtime.database.Ingredient;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class RecipeIngredientsFragment extends Fragment {

    private static final String SAVED_INSTANCE_INGREDINTS = "ingredients";

    private List<Ingredient> mIngredients;

    public RecipeIngredientsFragment() {
    }

    RecipeIngredientsFragment(List<Ingredient> ingredients) {
        mIngredients = ingredients;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(SAVED_INSTANCE_INGREDINTS)) {
            mIngredients = savedInstanceState.getParcelableArrayList(SAVED_INSTANCE_INGREDINTS);
        }

        if (mIngredients == null) {
            // TODO don't close the activity, show an error message
            ((RecipeStepActivity) requireActivity()).closeOnError();
            return null;
        }

        View rootView = inflater.inflate(R.layout.fragment_recipe_ingredients, container, false);
        TextView mIngredientsTv = rootView.findViewById(R.id.recipe_ingredients_list_tv);
        mIngredientsTv.setText(Utils.getIngredientsList(mIngredients));

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (mIngredients != null) {
            outState.putParcelableArrayList(SAVED_INSTANCE_INGREDINTS, new ArrayList<>(mIngredients));
        }
    }
}
