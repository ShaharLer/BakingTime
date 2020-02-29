package com.example.bakingtime.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.bakingtime.R;

public class RecipeDetailsActivity extends AppCompatActivity {

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        if (findViewById(R.id.line_seperator) != null) {
            mTwoPane = true;
        } else {
            mTwoPane = false;
        }
    }

    public void show(int position) {
        Log.d("TEST (DetailsActivity)", "Recipe detail number " + (position + 1) + " was clicked");
        Intent intent = new Intent(this, RecipeStepActivity.class);
        startActivity(intent);
    }
}
