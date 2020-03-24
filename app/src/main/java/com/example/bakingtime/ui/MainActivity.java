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
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bakingtime.IdlingResource.SimpleIdlingResource;
import com.example.bakingtime.R;
import com.example.bakingtime.Utils.StringUtils;
import com.example.bakingtime.database.Recipe;
import com.example.bakingtime.database.RecipesClient;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.IdlingResource;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements RecipesAdapter.RecipesAdapterOnClickHandler {

    private static final String BAKING_RECIPES_HTTP_URL = "https://d17h27t6h515a5.cloudfront.net/";
    private static final String SAVED_INSTANCE_RECIPES_LIST = "recipes list";
    private List<Recipe> mRecipes;
    private RecipesAdapter mRecipesAdapter;
    @BindView(R.id.recipes_list_layout) FrameLayout mRecipesListLayout;
    @BindView(R.id.rv_recipes) RecyclerView mRecipesRecyclerView;
    @BindView(R.id.error_layout) LinearLayout mErrorLayout;
    @BindView(R.id.pb_loading_indicator) ProgressBar mProgressBar;
    @BindView(R.id.my_toolbar) Toolbar mToolbar;
    @BindView(R.id.recipe_ingredients_container) FrameLayout mIngredientsListLayout;
    @Nullable
    private SimpleIdlingResource mIdlingResource; // The Idling Resource which will be null in production.

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getIdlingResource();

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            // In case the homescreen widget of the app was clicked
            homescreenWidgetProcess(savedInstanceState);
        } else {
            // In case the app was clicked
            appIconProcess(savedInstanceState);
        }
    }

    private void homescreenWidgetProcess(Bundle savedInstanceState) {
        mRecipesListLayout.setVisibility(View.GONE);
        getSupportActionBar().setTitle(PreferenceManager
                .getDefaultSharedPreferences(this)
                .getString(getString(R.string.pref_recipe_name_key), getString(R.string.pref_default_recipe_name)));
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_ingredients_container, new RecipeIngredientsFragment(true))
                    .commit();
        }
    }

    private void appIconProcess(Bundle savedInstanceState) {
        mIngredientsListLayout.setVisibility(View.GONE);
        if (savedInstanceState != null && savedInstanceState.containsKey(SAVED_INSTANCE_RECIPES_LIST)) {
            mRecipes = savedInstanceState.getParcelableArrayList(SAVED_INSTANCE_RECIPES_LIST);
            showRecipesList();
        } else {
            loadRecipesData();
        }
    }

    void closeOnIngredientsListError() {
        finish();
        Toast.makeText(this, R.string.recipe_ingredients_list_error, Toast.LENGTH_SHORT).show();
    }

    private void loadRecipesData() {
        setIdlingResource(false);
        hideData();
        Call<List<Recipe>> call = new Retrofit.Builder()
                .baseUrl(BAKING_RECIPES_HTTP_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RecipesClient.class)
                .bakingRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                mRecipes = response.body();
                showRecipesList();
                setIdlingResource(true);
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                showErrorMessage();
            }
        });
    }

    private void setIdlingResource(boolean state) {
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(state);
        }
    }

    private void hideData() {
        mRecipesRecyclerView.setVisibility(View.GONE);
        mErrorLayout.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void showRecipesList() {
        mRecipesAdapter = new RecipesAdapter(MainActivity.this, mRecipes);
        mRecipesRecyclerView.setAdapter(mRecipesAdapter);
        mRecipesRecyclerView.setHasFixedSize(true);
        mErrorLayout.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mRecipesRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecipesRecyclerView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mErrorLayout.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.refresh_button)
    public void refreshData(View view) {
        loadRecipesData();
    }

    @Override
    public void onRecipeClicked(int position) {
        Recipe chosenRecipe = mRecipesAdapter.getRecipes().get(position);
        String ingredientsList = StringUtils.getIngredientsList(chosenRecipe.getIngredients());
        PreferenceManager.getDefaultSharedPreferences(this).edit()
                .putString(getString(R.string.pref_recipe_name_key), chosenRecipe.getName())
                .putString(getString(R.string.pref_recipe_ingredients_list_key), ingredientsList)
                .apply();
        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, chosenRecipe);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mRecipes != null) {
            outState.putParcelableArrayList(SAVED_INSTANCE_RECIPES_LIST, new ArrayList<>(mRecipes));
        }
    }
}
