package com.example.bakingtime.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.bakingtime.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MasterListFragment extends Fragment implements MasterListAdapter.RecipesAdapterOnClickHandler {

    public MasterListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_recipes_list, container, false);
        RecyclerView mRecipesRecyclerView = rootView.findViewById(R.id.rv_recipes);
        mRecipesRecyclerView.setHasFixedSize(true);
        int columns = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) ? 1 : 2;
        GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), columns);;
        mRecipesRecyclerView.setLayoutManager(mLayoutManager);

        ArrayList<Integer> l = new ArrayList<>();
        l.add(1);
        l.add(2);
        l.add(3);
        l.add(4);
        l.add(5);
        l.add(6);
        l.add(7);
        l.add(8);
        MasterListAdapter mAdapter = new MasterListAdapter(this, l);
        mRecipesRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void OnRecipeClicked(int position) {
        Log.d("TEST (Fragment)", "Recipe " + (position + 1) + " was clicked");
        ((MainActivity) requireActivity()).show(position);
    }
}
