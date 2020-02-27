package com.example.bakingtime.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bakingtime.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class RecipesFragment extends Fragment implements RecipesAdapter.RecipesAdapterOnClickHandler {

    public RecipesFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_recipes_list, container, false);
        RecyclerView mRecipesRecyclerView = rootView.findViewById(R.id.rv_recipes);
        mRecipesRecyclerView.setHasFixedSize(true);
        ArrayList<Integer> l = new ArrayList<>();
        l.add(1);
        l.add(2);
        l.add(3);
        l.add(4);
        l.add(5);
        l.add(6);
        l.add(7);
        l.add(8);
        mRecipesRecyclerView.setAdapter(new RecipesAdapter(this, l));

        return rootView;
    }

    @Override
    public void OnRecipeClicked(int position) {
        Log.d("TEST (Recipes fragment)", "Recipe number " + (position + 1) + " was clicked");
        ((MainActivity) requireActivity()).show(position);
    }
}
