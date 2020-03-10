package com.example.bakingtime.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bakingtime.R;
import com.example.bakingtime.Utils;
import com.example.bakingtime.database.Recipe;
import com.example.bakingtime.database.Step;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class RecipeDetailsFragment extends Fragment implements RecipeDetailsAdapter.RecipeDetailsOnClickHandler {

    private static final String SAVED_INSTANCE_RECIPE_OBJECT = "recipe";
    private static final String SAVED_INSTANCE_TWO_PANE = "two pane";
    private static final String SAVED_INSTANCE_CHOSEN_POSITION = "chosne position";
    private RecipeDetailsAdapter mAdapter;
    private Recipe mRecipe;
    private boolean mTwoPane;
    private int mCurrentChosenPosition = 0;

    public RecipeDetailsFragment() {
    }

    RecipeDetailsFragment(Recipe recipe, boolean twoPane) {
        mRecipe = recipe;
        mTwoPane = twoPane;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mRecipe = savedInstanceState.getParcelable(SAVED_INSTANCE_RECIPE_OBJECT);
            mTwoPane = savedInstanceState.getBoolean(SAVED_INSTANCE_TWO_PANE);
            mCurrentChosenPosition = savedInstanceState.getInt(SAVED_INSTANCE_CHOSEN_POSITION);
        } else if (mTwoPane) {
            ((RecipeDetailsActivity) requireActivity()).createIngredientsFragment(mRecipe.getIngredients(), true);
        }

        if (mRecipe == null) {
            // TODO if mTwoPane don't close the activity, show an error message
            ((RecipeDetailsActivity) requireActivity()).closeOnError();
            return null;
        }

        View rootView = inflater.inflate(R.layout.fragment_recipe_details_list, container, false);
        RecyclerView mRecipesRecyclerView = rootView.findViewById(R.id.rv_recipe_details);
        mAdapter = new RecipeDetailsAdapter(this, mRecipe.getIngredients(), mRecipe.getSteps(), mTwoPane, mCurrentChosenPosition);
        mRecipesRecyclerView.setAdapter(mAdapter);
        mRecipesRecyclerView.setHasFixedSize(true);
        return rootView;
    }

    @Override
    public void onIngredientsClicked() {
        if (!mTwoPane) {
            new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                    .setMessage(Utils.getIngredientsList(mRecipe.getIngredients()))
                    .setNegativeButton(R.string.review_dialog_back_button, null)
                    .show();
        } else if (mAdapter.getCurrentChosenStepPosition() != 0) {
            ((RecipeDetailsActivity) requireActivity()).createIngredientsFragment(mRecipe.getIngredients(), false);
            mCurrentChosenPosition = 0;
            mAdapter.setCurrentChosenPosition(mCurrentChosenPosition);
        }
    }

    @Override
    public void onStepClicked(int chosenStepPosition) {
        Step chosenStep = mRecipe.getSteps().get(chosenStepPosition - 1);

        // TODO check if can bring this code back
//        int currentChosenStepPosition = mAdapter.getCurrentChosenStepPosition();
//        ((RecipeDetailsActivity) requireActivity()).onRecipeStepSelected(chosenStep, chosenStepPosition, currentChosenStepPosition, mAdapter);

        if (!mTwoPane) {
            Intent intent = new Intent(getActivity(), RecipeStepActivity.class);
            intent.putParcelableArrayListExtra(Intent.EXTRA_TEXT, new ArrayList<>(mRecipe.getSteps()));
            intent.putExtra(Intent.EXTRA_REFERRER, chosenStepPosition - 1);
            startActivity(intent);
        } else if (mAdapter.getCurrentChosenStepPosition() != chosenStepPosition) {
            ((RecipeDetailsActivity) requireActivity()).createStepFragment(chosenStep);
            mCurrentChosenPosition = chosenStepPosition;
            mAdapter.setCurrentChosenPosition(mCurrentChosenPosition);
        }
    }

    // TODO check if it a bad practice to put this functions here instead of in the activity
    /*
    private void createStepFragment(Step step, boolean addFragment) {
        RecipeStepFragment stepFragment = new RecipeStepFragment(step);
        FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        if (addFragment) {
            transaction.add(R.id.recipe_step_fragment_tablet, stepFragment);
        } else {
            transaction.replace(R.id.recipe_step_fragment_tablet, stepFragment);
        }
        transaction.commit();
    }
     */

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(SAVED_INSTANCE_RECIPE_OBJECT, mRecipe);
        outState.putBoolean(SAVED_INSTANCE_TWO_PANE, mTwoPane);
        outState.putInt(SAVED_INSTANCE_CHOSEN_POSITION, mCurrentChosenPosition);
    }
}
