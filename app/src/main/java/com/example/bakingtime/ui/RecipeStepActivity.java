package com.example.bakingtime.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.bakingtime.R;
import com.example.bakingtime.database.Step;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class RecipeStepActivity extends AppCompatActivity {

    private static final String SAVED_INSTANCE_STEPS_LIST = "steps list";
    private static final String SAVED_INSTANCE_STEP_NUMBER = "step number";
    private static final int STEP_NUMBER_DEFAULT = -1;
    private int mStepNumber;
    private List<Step> mSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);

        if (savedInstanceState != null
                && savedInstanceState.containsKey(SAVED_INSTANCE_STEPS_LIST)
                && savedInstanceState.containsKey(SAVED_INSTANCE_STEPS_LIST)) {

            mSteps = savedInstanceState.getParcelableArrayList(SAVED_INSTANCE_STEPS_LIST);
            mStepNumber = savedInstanceState.getInt(SAVED_INSTANCE_STEP_NUMBER);
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

        if ((mSteps == null) || (mStepNumber == STEP_NUMBER_DEFAULT)) {
            closeOnError();
            return;
        }

        createStepFragment(true);
    }

    void closeOnError() {
        finish();
        Toast.makeText(this, "Recipe data not available", Toast.LENGTH_SHORT).show();
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
        }
    }
}
