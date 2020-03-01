package com.example.bakingtime.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bakingtime.R;
import com.example.bakingtime.database.Recipe;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder> {

    private final RecipesAdapterOnClickHandler mClickHandler;
    private List<Recipe> mRecipesData;

    public interface RecipesAdapterOnClickHandler {
        void OnRecipeClicked(int position);
    }

    RecipesAdapter(RecipesAdapterOnClickHandler clickHandler, List<Recipe> recipesData) {
        mClickHandler = clickHandler;
        mRecipesData = recipesData;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recipes_list_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.recipeName.setText(mRecipesData.get(position).getName());
    }

    @Override
    public int getItemCount() {
        if (mRecipesData == null) return 0;
        return mRecipesData.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView recipeName;

        RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.recipe_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d("TEST (Recipes adapter)", "Recipe number " + (getAdapterPosition() + 1) + " was clicked");
            mClickHandler.OnRecipeClicked(getAdapterPosition());
        }
    }
}
