package com.example.bakingtime.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.bakingtime.R;
import com.example.bakingtime.database.Recipe;
import com.example.bakingtime.database.RecipesClient;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements RecipesAdapter.RecipesAdapterOnClickHandler {

    private static final String BAKING_RECIPES_HTTP_URL = "https://d17h27t6h515a5.cloudfront.net/";
    private RecipesAdapter mRecipesAdapter;
    private RecyclerView mRecipesRecyclerView;
    private ProgressBar mProgressBar;
    private LinearLayout mErrorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = findViewById(R.id.pb_loading_indicator);
        mErrorLayout = findViewById(R.id.error_layout);
        mRecipesRecyclerView = findViewById(R.id.rv_recipes);
        mRecipesRecyclerView.setHasFixedSize(true);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BAKING_RECIPES_HTTP_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        RecipesClient client = retrofit.create(RecipesClient.class);
        Call<List<Recipe>> call = client.bakingRecipes();
        hideData();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                List<Recipe> recipes = response.body();
                mRecipesAdapter = new RecipesAdapter(MainActivity.this, recipes);
                mRecipesRecyclerView.setAdapter(mRecipesAdapter);
                showData();
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                Log.d("TEST (Fragment)", "onFailure");
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
     * This method will make either the recipes list or the error message visible, and hide the
     * progress bar.
     */
    private void showData() {
        mErrorLayout.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mRecipesRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the recipes list.
     */
    private void showErrorMessage() {
        mRecipesRecyclerView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mErrorLayout.setVisibility(View.VISIBLE);
    }

    /**
     * This method is called whenever a recipe from the recipes list is clicked.
     *
     * @param position
     */
    @Override
    public void onRecipeClicked(int position) {
        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, mRecipesAdapter.getRecipes().get(position));
        startActivity(intent);
    }
}
