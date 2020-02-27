package com.example.bakingtime.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bakingtime.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecipeDetailsAdapter extends RecyclerView.Adapter<RecipeDetailsAdapter.RecipeDetailsViewHolder> {

    private final RecipeDetailsAdapterOnClickHandler mClickHandler;
    private List<Integer> mRecipesDetailsData;

    public interface RecipeDetailsAdapterOnClickHandler {
        void OnRecipeClicked(int position);
    }

    RecipeDetailsAdapter(RecipeDetailsAdapterOnClickHandler clickHandler, List<Integer> recipesDetailsData) {
        mClickHandler = clickHandler;
        mRecipesDetailsData = recipesDetailsData;
    }

    @NonNull
    @Override
    public RecipeDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recipe_details_list_item, parent, false);
        return new RecipeDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeDetailsViewHolder holder, int position) {
        if (position == 0) {
            holder.recipeDetail.setText("Recipe Ingredients");
        } else {
            holder.recipeDetail.setText("Recipe Step Description");
        }
    }

    @Override
    public int getItemCount() {
        if (mRecipesDetailsData == null) return 0;
        return mRecipesDetailsData.size();
    }

    public class RecipeDetailsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView recipeDetail;

        RecipeDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeDetail = itemView.findViewById(R.id.recipe_detail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d("TEST (Details adapter)", "Recipe detail number " + (getAdapterPosition() + 1) + " was clicked");
            mClickHandler.OnRecipeClicked(getAdapterPosition());
        }
    }

}
