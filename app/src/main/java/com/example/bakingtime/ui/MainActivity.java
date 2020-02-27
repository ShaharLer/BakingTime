package com.example.bakingtime.ui;

import android.os.Bundle;
import android.util.Log;

import com.example.bakingtime.R;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

//    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if (findViewById(R.id.recipe_step_linear_layout) != null) {
//            mTwoPane = true;
//            Log.d("TEST (MainActivity)", "mTwoPane = True");
//        }
    }

    public void show(int position) {
    }
}
