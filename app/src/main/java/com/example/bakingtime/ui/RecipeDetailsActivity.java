package com.example.bakingtime.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.bakingtime.R;
import com.example.bakingtime.database.Recipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class RecipeDetailsActivity extends AppCompatActivity {

    private boolean mTwoPane;
    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            if (intent == null) {
                closeOnError();
                return;
            }
            if (!intent.hasExtra(Intent.EXTRA_TEXT)) {
                closeOnError();
                return;
            }
            mRecipe = intent.getParcelableExtra(Intent.EXTRA_TEXT);

            // TODO check if must create a new fragment
            RecipeDetailsFragment detailsFragment = new RecipeDetailsFragment();
            detailsFragment.setRecipe(mRecipe);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_details_fragment, detailsFragment)
                    .commit();

            if (findViewById(R.id.recipe_step_fragment_tablet) != null) {
                mTwoPane = true;
                createStepFragment(0, true);
            } else {
                mTwoPane = false;
            }
        }
    }

    void closeOnError() {
        finish();
        Toast.makeText(this, "Recipe data not available", Toast.LENGTH_SHORT).show();
    }

    public void onRecipeStepSelected(Recipe recipe, int position) {
        if (mTwoPane) {
            createStepFragment(position, false);
        } else {
            Intent intent = new Intent(this, RecipeStepActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, recipe.getSteps().get(position));
            startActivity(intent);
        }
    }

    private void createStepFragment(int position, boolean addFragment) {
        RecipeStepFragment stepFragment = new RecipeStepFragment();
        stepFragment.setStep(mRecipe.getSteps().get(position));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (addFragment) {
            transaction.add(R.id.recipe_step_fragment_tablet, stepFragment);
        } else {
            transaction.replace(R.id.recipe_step_fragment_tablet, stepFragment);
        }
        transaction.commit();
    }
}
