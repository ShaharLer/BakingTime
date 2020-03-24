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
import com.example.bakingtime.utils.ActionBarUtils;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeIngredientsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_ingredients);
        setSupportActionBar(findViewById(R.id.my_toolbar));

        boolean fromWidget = false;
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            fromWidget = true;
            ActionBarUtils.setToolbarTitle(this, Objects.requireNonNull(getSupportActionBar()), false);
        } else {
            ActionBarUtils.setToolbarTitle(this, Objects.requireNonNull(getSupportActionBar()), true);
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_ingredients_container, new RecipeIngredientsFragment(fromWidget))
                    .commit();
        }
    }

    void closeOnError() {
        finish();
        Toast.makeText(this, R.string.recipe_ingredients_list_error, Toast.LENGTH_SHORT).show();
    }
}
