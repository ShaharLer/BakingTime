package com.example.bakingtime.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bakingtime.R;
import com.example.bakingtime.database.Ingredient;
import com.example.bakingtime.database.Step;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecipeDetailsAdapter extends RecyclerView.Adapter<RecipeDetailsAdapter.RecipeDetailsViewHolder> {

    private final RecipeDetailsOnClickHandler mClickHandler;
    private List<Ingredient> mIngredients;
    private List<Step> mSteps;

    public interface RecipeDetailsOnClickHandler {
        void onIngredientsClicked();

        void onStepClicked(int position);
    }

    RecipeDetailsAdapter(RecipeDetailsOnClickHandler clickHandler, List<Ingredient> ingredients, List<Step> recipesSteps) {
        mClickHandler = clickHandler;
        mIngredients = ingredients;
        mSteps = recipesSteps;
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
//            StringBuilder ingredients = new StringBuilder();
//            for (int i = 0; i < mIngredients.size(); i++) {
//                Ingredient ingredient = mIngredients.get(i);
//                ingredients.append(String.valueOf(ingredient.getQuantity())).append(" ")
//                        .append(ingredient.getMeasure()).append(" ")
//                        .append(ingredient.getIngredient());
//
//                if (i < mIngredients.size() - 1) {
//                    ingredients.append(", ");
//                }
//            }
            Ingredient ingredient = mIngredients.get(0);
            String ingredients = ingredient.getQuantity() + " " + ingredient.getMeasure() + " " + ingredient.getIngredient();
            holder.recipeDetail.setText(ingredients);
        } else {
            holder.recipeDetail.setText(mSteps.get(position - 1).getShortDescription());
        }
    }

    @Override
    public int getItemCount() {
        if (mIngredients == null || mSteps == null) return 0;
        return (mSteps.size() + 1); // 1 for the ingredients list
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
            int position = getAdapterPosition();
            if (position == 0) {
                mClickHandler.onIngredientsClicked();
            } else {
                mClickHandler.onStepClicked(getAdapterPosition() - 1);
            }
        }
    }

}
