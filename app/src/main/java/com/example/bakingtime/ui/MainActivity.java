package com.example.bakingtime.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bakingtime.R;

import java.util.List;

//public class MainActivity extends AppCompatActivity implements RecipesListFragment.OnRecipeClickListener {
public class MainActivity extends AppCompatActivity {

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.recipe_step_linear_layout) != null) {
            mTwoPane = true;
            Log.d("TEST (MainActivity)", "mTwoPane = True");
        }
    }

    public void show(int position) {
        Log.d("TEST (MainActivity)", "Recipe " + (position + 1) + " was clicked");
    }
}
