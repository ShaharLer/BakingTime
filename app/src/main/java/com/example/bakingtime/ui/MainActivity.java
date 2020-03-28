package com.example.bakingtime.ui;

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

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bakingtime.IdlingResource.SimpleIdlingResource;
import com.example.bakingtime.R;
import com.example.bakingtime.database.Recipe;
import com.example.bakingtime.database.RecipesClient;
import com.example.bakingtime.utils.StringUtils;
import com.example.bakingtime.widget.RecipeWidgetProvider;

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
    @BindView(R.id.rv_recipes) RecyclerView mRecipesRecyclerView;
    @BindView(R.id.error_layout) LinearLayout mErrorLayout;
    @BindView(R.id.pb_loading_indicator) ProgressBar mProgressBar;
    @BindView(R.id.my_toolbar) Toolbar mToolbar;
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

        if (savedInstanceState != null && savedInstanceState.containsKey(SAVED_INSTANCE_RECIPES_LIST)) {
            mRecipes = savedInstanceState.getParcelableArrayList(SAVED_INSTANCE_RECIPES_LIST);
            showRecipesList();
        } else {
            loadRecipesData();
        }
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
        List<String> ingredientsDetailsList = StringUtils.getIngredientsDetailsList(chosenRecipe.getIngredients());
        if (ingredientsDetailsList == null || ingredientsDetailsList.isEmpty()) {
            Toast.makeText(this, R.string.recipe_error, Toast.LENGTH_LONG).show();
            return;
        }

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString(getString(R.string.pref_recipe_name_key), chosenRecipe.getName());
        editor.putInt(getString(R.string.pref_recipe_ingredients_list_size_key), ingredientsDetailsList.size());
        String ingredientsListNameKey = getString(R.string.pref_recipe_ingredients_list_name_key);
        for (int i = 0; i < ingredientsDetailsList.size(); i++) {
            editor.putString(ingredientsListNameKey + i, ingredientsDetailsList.get(i));
        }
        editor.apply();

        // send broadcast to get the homescreen widget updated
        Intent widgetIntent = new Intent(this, RecipeWidgetProvider.class);
        widgetIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getApplication())
                .getAppWidgetIds(new ComponentName(getApplication(), RecipeWidgetProvider.class));
        widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(widgetIntent);

        Intent appIntent = new Intent(this, RecipeDetailsActivity.class);
        appIntent.putExtra(Intent.EXTRA_TEXT, chosenRecipe);
        startActivity(appIntent);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mRecipes != null) {
            outState.putParcelableArrayList(SAVED_INSTANCE_RECIPES_LIST, new ArrayList<>(mRecipes));
        }
    }
}
