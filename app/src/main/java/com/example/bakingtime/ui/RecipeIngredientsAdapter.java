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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bakingtime.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeIngredientsAdapter extends RecyclerView.Adapter<RecipeIngredientsAdapter.RecipeIngredientsViewHolder> {

    private List<String> mIngredients;

    RecipeIngredientsAdapter(List<String> ingredients) {
        mIngredients = ingredients;
    }

    @NonNull
    @Override
    public RecipeIngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recipe_ingredients_list_item, parent, false);
        return new RecipeIngredientsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeIngredientsViewHolder holder, int position) {
        holder.ingredientDetails.setText(mIngredients.get(position));
    }

    @Override
    public int getItemCount() {
        if (mIngredients == null) return 0;
        return mIngredients.size();
    }

    static class RecipeIngredientsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ingredient_details) TextView ingredientDetails;

        RecipeIngredientsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
