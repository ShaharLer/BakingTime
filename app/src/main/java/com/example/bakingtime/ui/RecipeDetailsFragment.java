package com.example.bakingtime.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bakingtime.R;
import com.example.bakingtime.database.Recipe;
import com.example.bakingtime.database.Step;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class RecipeDetailsFragment extends Fragment implements RecipeDetailsAdapter.RecipeDetailsOnClickHandler {

    private static final String SAVED_INSTANCE_RECIPE_OBJECT = "recipe";
    private static final String SAVED_INSTANCE_TWO_PANE = "two pane";
    private static final String SAVED_INSTANCE_CHOSEN_POSITION = "chosne position";

    private RecipeDetailsAdapter mAdapter;
    private Recipe mRecipe;
    private boolean mTwoPane;
    private int mCurrentChosenPosition;

    public RecipeDetailsFragment() {
    }

    RecipeDetailsFragment(Recipe recipe) {
        mRecipe = recipe;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mRecipe = savedInstanceState.getParcelable(SAVED_INSTANCE_RECIPE_OBJECT);
            mTwoPane = savedInstanceState.getBoolean(SAVED_INSTANCE_TWO_PANE);
            mCurrentChosenPosition = savedInstanceState.getInt(SAVED_INSTANCE_CHOSEN_POSITION);
        } else if (Objects.requireNonNull(getActivity()).findViewById(R.id.recipe_step_fragment_tablet) != null) {
            mTwoPane = true;
            ((RecipeDetailsActivity) requireActivity()).createIngredientsFragment(mRecipe.getIngredients(), true);
        }

        if (mRecipe == null) {
            ((RecipeDetailsActivity) requireActivity()).closeOnError();
            return null;
        }

        mAdapter = new RecipeDetailsAdapter(this, mRecipe.getIngredients(), mRecipe.getSteps(), mTwoPane, mCurrentChosenPosition);
        View rootView = inflater.inflate(R.layout.fragment_recipe_details_list, container, false);
        RecyclerView mRecipesRecyclerView = rootView.findViewById(R.id.rv_recipe_details);
        mRecipesRecyclerView.setAdapter(mAdapter);
        mRecipesRecyclerView.setHasFixedSize(true);
        return rootView;
    }

    @Override
    public void onIngredientsClicked() {
        if (!mTwoPane) {
            startActivity(new Intent(getActivity(), RecipeIngredientsActivity.class));
        } else if (mAdapter.getCurrentChosenStepPosition() != 0) {
            ((RecipeDetailsActivity) requireActivity()).createIngredientsFragment(mRecipe.getIngredients(), false);
            mAdapter.setCurrentChosenPosition(mCurrentChosenPosition = 0);
        }
    }

    @Override
    public void onStepClicked(int chosenStepPosition) {
        Step chosenStep = mRecipe.getSteps().get(chosenStepPosition - 1);
        if (!mTwoPane) {
            Intent intent = new Intent(getActivity(), RecipeStepActivity.class);
            intent.putParcelableArrayListExtra(Intent.EXTRA_TEXT, new ArrayList<>(mRecipe.getSteps()));
            intent.putExtra(Intent.EXTRA_REFERRER, chosenStepPosition - 1);
            intent.putExtra(Intent.EXTRA_INTENT, mRecipe.getName());
            startActivity(intent);
        } else if (mAdapter.getCurrentChosenStepPosition() != chosenStepPosition) {
            ((RecipeDetailsActivity) requireActivity()).createStepFragment(chosenStep);
            mAdapter.setCurrentChosenPosition(mCurrentChosenPosition = chosenStepPosition);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(SAVED_INSTANCE_RECIPE_OBJECT, mRecipe);
        outState.putBoolean(SAVED_INSTANCE_TWO_PANE, mTwoPane);
        outState.putInt(SAVED_INSTANCE_CHOSEN_POSITION, mCurrentChosenPosition);
    }
}
