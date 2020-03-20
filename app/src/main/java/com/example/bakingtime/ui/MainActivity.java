package com.example.bakingtime.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bakingtime.R;
import com.example.bakingtime.Utils;
import com.example.bakingtime.database.Recipe;
import com.example.bakingtime.database.RecipesClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
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

    //    @BindView(R.id.recipes_list_layout)
    FrameLayout mRecipesListLayout;

    //    @BindView(R.id.rv_recipes)
    RecyclerView mRecipesRecyclerView;

    //    @BindView(R.id.error_layout)
    LinearLayout mErrorLayout;

    //    @BindView(R.id.pb_loading_indicator)
    ProgressBar mProgressBar;

    //    @BindView(R.id.recipe_ingredients_fragment_widget)
    FrameLayout mIngredientsListLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ButterKnife.bind(this);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        mRecipesListLayout = findViewById(R.id.recipes_list_layout);
        mRecipesRecyclerView = findViewById(R.id.rv_recipes);
        mErrorLayout = findViewById(R.id.error_layout);
        mProgressBar = findViewById(R.id.pb_loading_indicator);
        mIngredientsListLayout = findViewById(R.id.recipe_ingredients_fragment_widget);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            // Got here from the homescreen widget
            mRecipesListLayout.setVisibility(View.GONE);
            if (savedInstanceState == null) {
                showLastSeenRecipeIngredients();
            } else {
                setToolBarTitle();
            }
        } else {
            // Got here from the app icon
            mIngredientsListLayout.setVisibility(View.GONE);
            if (savedInstanceState != null && savedInstanceState.containsKey(SAVED_INSTANCE_RECIPES_LIST)) {
                mRecipes = savedInstanceState.getParcelableArrayList(SAVED_INSTANCE_RECIPES_LIST);
                showRecipesList();
            } else {
                loadRecipesData();
            }
        }
    }

    /**
     * This method is called if the homescreen widget of the app was cicked.
     * It shows the ingredients list of the recipe that was last seen, otherwise shows an
     * informative message.
     */
    private void showLastSeenRecipeIngredients() {
        setToolBarTitle();
        RecipeIngredientsFragment ingredientsFragment = new RecipeIngredientsFragment(true);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.recipe_ingredients_fragment_widget, ingredientsFragment)
                .commit();
    }

    /**
     * This method closes the acrivity when the ingredients list is not available.
     */
    void closeOnIngredientsListError() {
        finish();
        Toast.makeText(this, R.string.recipe_ingredients_list_error, Toast.LENGTH_SHORT).show();
    }

    /**
     * This method sets the toolbar title in case this activity was called from the homescreen
     * widget.
     */
    private void setToolBarTitle() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String recipeName = sharedPreferences.getString(getString(R.string.pref_recipe_name_key),
                getString(R.string.pref_default_recipe_name));
        Objects.requireNonNull(getSupportActionBar()).setTitle(recipeName);
    }

    private void loadRecipesData() {
        hideData();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BAKING_RECIPES_HTTP_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        RecipesClient client = retrofit.create(RecipesClient.class);
        Call<List<Recipe>> call = client.bakingRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                mRecipes = response.body();
                showRecipesList();
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                showErrorMessage();
            }
        });
    }

    /**
     * This method will hide the data views and show the progress bar.
     */
    private void hideData() {
        mRecipesRecyclerView.setVisibility(View.GONE);
        mErrorLayout.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the recipes list visible.
     */
    private void showRecipesList() {
        mRecipesAdapter = new RecipesAdapter(MainActivity.this, mRecipes);
        mRecipesRecyclerView.setAdapter(mRecipesAdapter);
        mRecipesRecyclerView.setHasFixedSize(true);
        mErrorLayout.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mRecipesRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible.
     */
    private void showErrorMessage() {
        mRecipesRecyclerView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mErrorLayout.setVisibility(View.VISIBLE);
    }

    /**
     * The method makes the app execute again the loadRecipesData function when the user clicks the
     * REFRESH button, after an error occurs.
     *
     * @param view The view of the REFRESH button.
     */
    public void refreshData(View view) {
        loadRecipesData();
    }

    /**
     * This method is called whenever a recipe from the recipes list is clicked.
     *
     * @param position - The position of the recipe that was clicked.
     */
    @Override
    public void onRecipeClicked(int position) {
        Recipe chosenRecipe = mRecipesAdapter.getRecipes().get(position);
        String ingredientsList = Utils.getIngredientsList(chosenRecipe.getIngredients());
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
