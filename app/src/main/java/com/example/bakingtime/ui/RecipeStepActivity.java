package com.example.bakingtime.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.bakingtime.R;
import com.example.bakingtime.database.Step;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeStepActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);

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

            RecipeStepFragment stepFragment = new RecipeStepFragment();
            Step step = intent.getParcelableExtra(Intent.EXTRA_TEXT);
            stepFragment.setStep(step);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_step_fragment, stepFragment)
                    .commit();
        }
    }

    void closeOnError() {
        finish();
        Toast.makeText(this, "Recipe data not available", Toast.LENGTH_SHORT).show();
    }
}
