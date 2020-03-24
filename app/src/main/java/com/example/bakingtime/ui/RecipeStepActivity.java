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

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.bakingtime.R;
import com.example.bakingtime.Utils.ActionBarUtils;
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
    private List<Step> mSteps;
    private int mStepNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);
        setSupportActionBar(findViewById(R.id.my_toolbar));
        ActionBarUtils.setToolbarTitle(this, getSupportActionBar());

        if (savedInstanceState != null && savedInstanceState.containsKey(SAVED_INSTANCE_STEPS_LIST)) {
            mSteps = savedInstanceState.getParcelableArrayList(SAVED_INSTANCE_STEPS_LIST);
            mStepNumber = savedInstanceState.getInt(SAVED_INSTANCE_STEP_NUMBER);
        } else {
            Intent intent = getIntent();
            if (intent == null || !intent.hasExtra(Intent.EXTRA_TEXT) || !intent.hasExtra(Intent.EXTRA_REFERRER)) {
                closeOnError();
                return;
            }
            mSteps = intent.getParcelableArrayListExtra(Intent.EXTRA_TEXT);
            mStepNumber = intent.getIntExtra(Intent.EXTRA_REFERRER, STEP_NUMBER_DEFAULT);
            if (mSteps == null || mStepNumber == STEP_NUMBER_DEFAULT) {
                closeOnError();
                return;
            }

            createStepFragment(true);
        }
    }

    void closeOnError() {
        finish();
        Toast.makeText(this, R.string.recipe_step_error, Toast.LENGTH_SHORT).show();
    }

    public void onPrevNextStepsClicked(boolean add) {
        mStepNumber += (add) ? 1 : (-1);
        createStepFragment(false);
    }

    private void createStepFragment(boolean addFragment) {
        RecipeStepFragment stepFragment;
        if (mStepNumber == 0) {
            stepFragment = new RecipeStepFragment(mSteps.get(mStepNumber), false, true);
        } else if (mStepNumber < mSteps.size() - 1) {
            stepFragment = new RecipeStepFragment(mSteps.get(mStepNumber), true, true);
        } else {
            stepFragment = new RecipeStepFragment(mSteps.get(mStepNumber), true, false);
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
