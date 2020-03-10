package com.example.bakingtime.ui;

import android.graphics.Color;
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
    private boolean mTwoPane;
    private int mCurrentChosenPosition;

    public interface RecipeDetailsOnClickHandler {
        void onIngredientsClicked();
        void onStepClicked(int position);
    }

    RecipeDetailsAdapter(RecipeDetailsOnClickHandler clickHandler, List<Ingredient> ingredients,
                         List<Step> recipesSteps, boolean twoPane, int currentChosenPosition) {
        mClickHandler = clickHandler;
        mIngredients = ingredients;
        mSteps = recipesSteps;
        mTwoPane = twoPane;
        mCurrentChosenPosition = currentChosenPosition;
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
        TextView view = holder.recipeDetail;
        if (position == 0) {
            view.setText(view.getContext().getResources().getString(R.string.ingredients_list_headline));
        } else {
            view.setText(mSteps.get(position - 1).getShortDescription());
        }

        if (mTwoPane) {
            if (position == mCurrentChosenPosition) {
                view.setBackgroundColor(Color.BLUE);
            } else {
                int defaultBackgroundColor = view.getContext().getResources().getColor(R.color.itemBackgroundColor);
                view.setBackgroundColor(defaultBackgroundColor);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mIngredients == null || mSteps == null) return 0;
        return (mSteps.size() + 1); // 1 for the ingredients list
    }

    int getCurrentChosenStepPosition() {
        return mCurrentChosenPosition;
    }

    void setCurrentChosenPosition(int position) {
        mCurrentChosenPosition = position;
        notifyDataSetChanged();
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
                mClickHandler.onStepClicked(position);
            }
        }
    }
}
