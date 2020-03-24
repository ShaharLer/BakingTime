/*
    Copyright (C) 2020 The Android Open Source Project

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

package com.example.bakingtime.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.bakingtime.R;
import com.example.bakingtime.Utils.ActionBarUtils;
import com.example.bakingtime.database.Recipe;
import com.example.bakingtime.database.Step;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class RecipeDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        setSupportActionBar(findViewById(R.id.my_toolbar));
        ActionBarUtils.setToolbarTitle(this, getSupportActionBar());

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            if (intent == null || !intent.hasExtra(Intent.EXTRA_TEXT) || intent.getParcelableExtra(Intent.EXTRA_TEXT) == null) {
                closeOnError();
                return;
            }

            Recipe recipe = intent.getParcelableExtra(Intent.EXTRA_TEXT);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_details_fragment, new RecipeDetailsFragment(recipe))
                    .commit();
        }
    }

    void closeOnError() {
        finish();
        Toast.makeText(this, R.string.recipe_error, Toast.LENGTH_SHORT).show();
    }

    public void createStepFragment(Step step) {
        makeFragmentTransaction(new RecipeStepFragment(step, false, false), false);
    }

    public void createIngredientsFragment(boolean addFragment) {
        makeFragmentTransaction(new RecipeIngredientsFragment(false), addFragment);
    }

    private void makeFragmentTransaction(Fragment fragment, boolean addFragment) {
        if (addFragment) {
            getSupportFragmentManager().beginTransaction().add(R.id.recipe_step_fragment_tablet, fragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.recipe_step_fragment_tablet, fragment).commit();
        }
    }
}
