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
import butterknife.BindView;
import butterknife.ButterKnife;

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
                view.setBackground(view.getContext().getDrawable(R.drawable.recipe_detail_chosen_background));
            } else {
                view.setBackground(view.getContext().getDrawable(R.drawable.text_view_background));
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mIngredients == null || mSteps == null) return 0;
        return (mSteps.size() + 1); // +1 for the ingredients list
    }

    int getCurrentChosenStepPosition() {
        return mCurrentChosenPosition;
    }

    void setCurrentChosenPosition(int position) {
        mCurrentChosenPosition = position;
        notifyDataSetChanged();
    }

    public class RecipeDetailsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.recipe_detail) TextView recipeDetail;

        RecipeDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (getAdapterPosition() == 0) {
                mClickHandler.onIngredientsClicked();
            } else {
                mClickHandler.onStepClicked(getAdapterPosition());
            }
        }
    }
}
