package com.example.bakingtime.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bakingtime.R;
import com.example.bakingtime.database.Recipe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class RecipeDetailsFragment extends Fragment implements RecipeDetailsAdapter.RecipeDetailsOnClickHandler {

    private Recipe mRecipe;

    public RecipeDetailsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRecipe == null) {
            ((RecipeDetailsActivity) requireActivity()).closeOnError();
            getActivity().finish();
            return null;
        }

        final View rootView = inflater.inflate(R.layout.fragment_recipe_details_list, container, false);
        RecyclerView mRecipesRecyclerView = rootView.findViewById(R.id.rv_recipe_details);
        mRecipesRecyclerView.setAdapter(new RecipeDetailsAdapter(this, mRecipe.getIngredients(), mRecipe.getSteps()));
        mRecipesRecyclerView.setHasFixedSize(true);
        return rootView;
    }

    void setRecipe(Recipe recipe) {
        this.mRecipe = recipe;
    }

    @Override
    public void onIngredientsClicked() {
        Log.d("TEST (Details fragment)", "Recipe ingredients was clicked");
        Toast.makeText(getContext(), "Clicked on the recipe ingredients", Toast.LENGTH_LONG).show();
//        ((RecipeDetailsActivity) requireActivity()).show(0);
    }

    @Override
    public void onStepClicked(int position) {
        ((RecipeDetailsActivity) requireActivity()).onRecipeStepSelected(mRecipe, position);
    }
}
