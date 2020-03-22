package com.example.bakingtime.ui;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.example.bakingtime.R;
import com.example.bakingtime.database.Ingredient;
import com.example.bakingtime.database.Recipe;
import com.example.bakingtime.database.Step;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
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

            mRecipeName = PreferenceManager.getDefaultSharedPreferences(this)
                    .getString(getString(R.string.pref_recipe_name_key), getString(R.string.pref_default_recipe_name));
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_details_fragment, new RecipeDetailsFragment(recipe))
                    .commit();
        } else {
            mRecipeName = savedInstanceState.getString(SAVED_INSTANCE_RECIPE_NAME);
        }

        setToolbar();
    }

    private void setToolbar() {
        Toolbar mToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = Objects.requireNonNull(getSupportActionBar());
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(mRecipeName);
    }

    void closeOnError() {
        finish();
        Toast.makeText(this, R.string.recipe_error, Toast.LENGTH_SHORT).show();
    }

    public void createStepFragment(Step step) {
        makeFragmentTransaction(new RecipeStepFragment(step, false, false), false);
    }

    public void createIngredientsFragment(List<Ingredient> ingredients, boolean addFragment) {
        makeFragmentTransaction(new RecipeIngredientsFragment(false), addFragment);
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
