package com.example.bakingtime.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bakingtime.R;
import com.example.bakingtime.database.Step;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class RecipeStepFragment extends Fragment {

    private Step mStep;

    public RecipeStepFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mStep == null) {
            ((RecipeStepActivity) requireActivity()).closeOnError();
            return null;
        }

        final View rootView = inflater.inflate(R.layout.fragment_recipe_step, container, false);

        TextView recipeStepVideo = rootView.findViewById(R.id.recipe_step_video);
        TextView recipeStepDescription = rootView.findViewById(R.id.recipe_step_description);
        recipeStepVideo.setText(mStep.getVideoURL());
        recipeStepDescription.setText(mStep.getDescription());

        return rootView;
    }

    void setStep(Step step) {
        this.mStep = step;
    }
}
