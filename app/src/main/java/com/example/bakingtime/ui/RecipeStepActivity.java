package com.example.bakingtime.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.bakingtime.R;
import com.example.bakingtime.database.Step;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

public class RecipeStepActivity extends AppCompatActivity {

    private static final String SAVED_INSTANCE_STEPS_LIST = "steps list";
    private static final String SAVED_INSTANCE_STEP_NUMBER = "step number";
    private static final String SAVED_INSTANCE_RECIPE_NAME = "reipce name";
    private static final int STEP_NUMBER_DEFAULT = -1;
    private List<Step> mSteps;
    private int mStepNumber;
    private String mRecipeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);

        Toolbar mToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState != null && savedInstanceState.containsKey(SAVED_INSTANCE_STEPS_LIST)) {
            mSteps = savedInstanceState.getParcelableArrayList(SAVED_INSTANCE_STEPS_LIST);
            mStepNumber = savedInstanceState.getInt(SAVED_INSTANCE_STEP_NUMBER);
            mRecipeName = savedInstanceState.getString(SAVED_INSTANCE_RECIPE_NAME);
            setActionBarTitle();
            return;
        }

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
            return;
        }

        if (!intent.hasExtra(Intent.EXTRA_TEXT) || !intent.hasExtra(Intent.EXTRA_REFERRER)) {
            closeOnError();
            return;
        }

        mSteps = intent.getParcelableArrayListExtra(Intent.EXTRA_TEXT);
        mStepNumber = intent.getIntExtra(Intent.EXTRA_REFERRER, STEP_NUMBER_DEFAULT);
        mRecipeName = intent.getStringExtra(Intent.EXTRA_INTENT);

        if (mSteps == null || mStepNumber == STEP_NUMBER_DEFAULT || mRecipeName == null) {
            closeOnError();
            return;
        }

        setActionBarTitle();
        createStepFragment(true);
    }

    private void setActionBarTitle() {
        Objects.requireNonNull(getSupportActionBar()).setTitle(mRecipeName);
    }

    void closeOnError() {
        finish();
        Toast.makeText(this, R.string.recipe_step_data_error, Toast.LENGTH_SHORT).show();
    }

    public void onPrevNextStepsClicked(boolean add) {
        if (add) {
            mStepNumber++;
        } else {
            mStepNumber--;
        }
        createStepFragment(false);
    }

    private void createStepFragment(boolean addFragment) {
        Step step = mSteps.get(mStepNumber);

        RecipeStepFragment stepFragment;
        if (mStepNumber == 0) {
            stepFragment = new RecipeStepFragment(step, false, true);
        } else if (mStepNumber < mSteps.size() - 1) {
            stepFragment = new RecipeStepFragment(step, true, true);
        } else {
            stepFragment = new RecipeStepFragment(step, true, false);
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (addFragment) {
            transaction.add(R.id.recipe_step_fragment, stepFragment);
        } else {
            transaction.replace(R.id.recipe_step_fragment, stepFragment);
        }
        transaction.commit();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mSteps != null && mStepNumber != STEP_NUMBER_DEFAULT) {
            outState.putParcelableArrayList(SAVED_INSTANCE_STEPS_LIST, new ArrayList<>(mSteps));
            outState.putInt(SAVED_INSTANCE_STEP_NUMBER, mStepNumber);
            outState.putString(SAVED_INSTANCE_RECIPE_NAME, mRecipeName);
        }
    }
}
