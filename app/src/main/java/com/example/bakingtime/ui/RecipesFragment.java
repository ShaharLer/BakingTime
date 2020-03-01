package com.example.bakingtime.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.bakingtime.R;
import com.example.bakingtime.database.RecipesClient;
import com.example.bakingtime.database.Recipe;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipesFragment extends Fragment implements RecipesAdapter.RecipesAdapterOnClickHandler {

    private static final String BAKING_RECIPES_HTTP_URL = "https://d17h27t6h515a5.cloudfront.net/";
    private RecyclerView mRecipesRecyclerView;
    private ProgressBar mProgressBar;
    private LinearLayout mErrorLayout;

    public RecipesFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_recipes_list, container, false);
        mProgressBar = rootView.findViewById(R.id.pb_loading_indicator);
        mErrorLayout = rootView.findViewById(R.id.error_layout);
        mRecipesRecyclerView = rootView.findViewById(R.id.rv_recipes);
        mRecipesRecyclerView.setHasFixedSize(true);
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
                List<Recipe> recipes = response.body();
                mRecipesRecyclerView.setAdapter(new RecipesAdapter(RecipesFragment.this, recipes));
                showData();
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                Log.d("TEST (Fragment)", "onFailure");
                showErrorMessage();
            }
        });

        return rootView;
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
     * This method will show either the movie list or the error message visible and hide the
     * progress bar.
     */
    private void showData() {
        mErrorLayout.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mRecipesRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the movies View.
     */
    private void showErrorMessage() {
        mRecipesRecyclerView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mErrorLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void OnRecipeClicked(int position) {
        Log.d("TEST (Recipes fragment)", "Recipe number " + (position + 1) + " was clicked");
        ((MainActivity) requireActivity()).show(position);
    }
}
