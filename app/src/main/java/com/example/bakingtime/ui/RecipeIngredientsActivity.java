package com.example.bakingtime.ui;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.example.bakingtime.R;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class RecipeIngredientsActivity extends AppCompatActivity {

    private static final String SAVED_INSTANCE_RECIPE_NAME = "recipe name";
    private String mRecipeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_ingredients);

        if (savedInstanceState == null) {
            mRecipeName = PreferenceManager.getDefaultSharedPreferences(this)
                    .getString(getString(R.string.pref_recipe_name_key), getString(R.string.pref_default_recipe_name));
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_ingredients_fragment, new RecipeIngredientsFragment(false))
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
        Toast.makeText(this, R.string.recipe_ingredients_list_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVED_INSTANCE_RECIPE_NAME, mRecipeName);
    }
}
