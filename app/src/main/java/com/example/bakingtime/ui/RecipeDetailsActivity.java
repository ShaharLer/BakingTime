package com.example.bakingtime.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.bakingtime.R;
import com.example.bakingtime.database.Ingredient;
import com.example.bakingtime.database.Recipe;
import com.example.bakingtime.database.Step;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class RecipeDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        boolean mTwoPane = findViewById(R.id.recipe_step_fragment_tablet) != null;
        // TODO check if I can remove this from here and do all the work in the fragment
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
            Recipe recipe = intent.getParcelableExtra(Intent.EXTRA_TEXT);
            if (recipe == null) {
                closeOnError();
                return;
            }

            // TODO check if must create a new fragment
            RecipeDetailsFragment detailsFragment = new RecipeDetailsFragment(recipe, mTwoPane);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_details_fragment, detailsFragment)
                    .commit();
        }
    }

    void closeOnError() {
        finish();
        Toast.makeText(this, "Recipe data not available", Toast.LENGTH_SHORT).show();
    }

    public void createStepFragment(Step step) {
        RecipeStepFragment stepFragment = new RecipeStepFragment(step, false, false);
        makeFragmentTransaction(stepFragment, false);
    }

    public void createIngredientsFragment(List<Ingredient> ingredients, boolean addFragment) {
        RecipeIngredientsFragment ingredientsFragment = new RecipeIngredientsFragment(ingredients);
        makeFragmentTransaction(ingredientsFragment, addFragment);
    }

    private void makeFragmentTransaction(Fragment fragment, boolean addFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (addFragment) {
            transaction.add(R.id.recipe_step_fragment_tablet, fragment);
        } else {
            transaction.replace(R.id.recipe_step_fragment_tablet, fragment);
        }
        transaction.commit();
    }
}
