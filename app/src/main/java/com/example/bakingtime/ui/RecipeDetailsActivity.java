package com.example.bakingtime.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.bakingtime.R;
import com.example.bakingtime.database.Ingredient;
import com.example.bakingtime.database.Recipe;
import com.example.bakingtime.database.Step;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class RecipeDetailsActivity extends AppCompatActivity {

    private static final String SAVED_INSTANCE_RECIPE_NAME = "recipe name";
    private String mRecipeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        Toolbar mToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

            mRecipeName = recipe.getName();

            RecipeDetailsFragment detailsFragment = new RecipeDetailsFragment(recipe);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_details_fragment, detailsFragment)
                    .commit();
        } else {
            mRecipeName = savedInstanceState.getString(SAVED_INSTANCE_RECIPE_NAME);
        }

        Objects.requireNonNull(getSupportActionBar()).setTitle(mRecipeName);
    }

    void closeOnError() {
        finish();
        Toast.makeText(this, R.string.recipe_detail_data_error, Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVED_INSTANCE_RECIPE_NAME, mRecipeName);
    }
}
