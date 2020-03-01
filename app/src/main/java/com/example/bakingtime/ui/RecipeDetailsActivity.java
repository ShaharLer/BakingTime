package com.example.bakingtime.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.bakingtime.R;
import com.example.bakingtime.database.Recipe;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailsActivity extends AppCompatActivity {

//    private boolean mTwoPane;

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

            RecipeDetailsFragment detailsFragment = new RecipeDetailsFragment();
            Recipe recipe = intent.getParcelableExtra(Intent.EXTRA_TEXT);
            detailsFragment.setRecipe(recipe);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_details_fragment, detailsFragment)
                    .commit();
        }

//        if (findViewById(R.id.line_seperator) != null) {
//            mTwoPane = true;
//        } else {
//            mTwoPane = false;
//        }
    }

    void closeOnError() {
        finish();
        Toast.makeText(this, "Recipe data not available", Toast.LENGTH_SHORT).show();
    }

    public void show(int position) {
        Log.d("TEST (DetailsActivity)", "Recipe detail number " + (position + 1) + " was clicked");
        Intent intent = new Intent(this, RecipeStepActivity.class);
        startActivity(intent);
    }
}
