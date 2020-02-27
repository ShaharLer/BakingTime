package com.example.bakingtime.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bakingtime.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MasterListAdapter extends RecyclerView.Adapter<MasterListAdapter.RecipeViewHolder> {

    private final RecipesAdapterOnClickHandler mClickHandler;
    private List<Integer> mRecipesData;

    public interface RecipesAdapterOnClickHandler {
        void OnRecipeClicked(int position);
    }

    MasterListAdapter(RecipesAdapterOnClickHandler clickHandler, List<Integer> recipesData) {
        mClickHandler = clickHandler;
        mRecipesData = recipesData;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recipe_list_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.recipeName.setText("Nutella Pie");
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
            mClickHandler.OnRecipeClicked(getAdapterPosition());
        }
    }
}
