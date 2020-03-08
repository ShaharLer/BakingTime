package com.example.bakingtime.ui;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.bakingtime.R;
import com.example.bakingtime.database.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class RecipeStepFragment extends Fragment {

    private static final String SAVED_INSTANCE_STEP_OBJECT = "step";
    private static final String SAVED_INSTANCE_HAS_PREV = "prev";
    private static final String SAVED_INSTANCE_HAS_NEXT = "next";

    private Step mStep;
    private boolean mHasPrev;
    private boolean mHasNext;
    private View rootView;
    private String mVideoUrl;
    //    private Unbinder unbinder;
    private ExoPlayer mExoPlayer;
    //    @BindView(R.id.recipe_step_video)
    private PlayerView mPlayerView;
    //    @BindView(R.id.recipe_step_description)
    private TextView mRecipeStepDescription;
    //    @BindView(R.id.recipe_next_prev_step)
    private Button mPrevStepButton;
    private Button mNextStepButton;

    public RecipeStepFragment() {
    }

    RecipeStepFragment(Step step, boolean hasPrev, boolean hasNext) {
        mStep = step;
        mHasPrev = hasPrev;
        mHasNext = hasNext;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null
                && savedInstanceState.containsKey(SAVED_INSTANCE_STEP_OBJECT)
                && savedInstanceState.containsKey(SAVED_INSTANCE_HAS_PREV)
                && savedInstanceState.containsKey(SAVED_INSTANCE_HAS_NEXT)) {

            mStep = savedInstanceState.getParcelable(SAVED_INSTANCE_STEP_OBJECT);
            mHasPrev = savedInstanceState.getBoolean(SAVED_INSTANCE_HAS_PREV);
            mHasNext = savedInstanceState.getBoolean(SAVED_INSTANCE_HAS_NEXT);
        }

        // TODO act different whether on tablet or not
        if (mStep == null) {
            ((RecipeStepActivity) requireActivity()).closeOnError();
            return null;
        }

        int smallestScreenWidthDp = getResources().getConfiguration().smallestScreenWidthDp;
        int orientation = getResources().getConfiguration().orientation;

        if (smallestScreenWidthDp >= 600) { // TODO move 600 to dimens
            rootView = inflater.inflate(R.layout.fragment_recipe_step_tablet, container, false);
            mRecipeStepDescription = rootView.findViewById(R.id.recipe_step_description);
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            rootView = inflater.inflate(R.layout.fragment_recipe_step, container, false);
            mRecipeStepDescription = rootView.findViewById(R.id.recipe_step_description);
            mPrevStepButton = rootView.findViewById(R.id.recipe_prev_step);
            mNextStepButton = rootView.findViewById(R.id.recipe_next_step);
            setClickListener(mPrevStepButton, mHasPrev, false);
            setClickListener(mNextStepButton, mHasNext, true);
        } else {
            rootView = inflater.inflate(R.layout.fragment_recipe_step_land, container, false);
            View decorView = Objects.requireNonNull(getActivity()).getWindow().getDecorView();
            // Hide the status bar.
            decorView.setSystemUiVisibility(
                    // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            // Hide the nav bar and status bar
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }

//        unbinder = ButterKnife.bind(this, rootView);

        if (mRecipeStepDescription != null) {
            mRecipeStepDescription.setText(mStep.getDescription());
        }
        mPlayerView = rootView.findViewById(R.id.recipe_step_video);
        mVideoUrl = !mStep.getVideoURL().isEmpty() ? mStep.getVideoURL() : mStep.getThumbnailURL();

        initializePlayer();
        return rootView;
    }

    private void setClickListener(Button button, boolean showButton, boolean isNext) {
        if (showButton) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((RecipeStepActivity) requireActivity()).onPrevNextStepsClicked(isNext);
                }
            });
        } else {
            button.setVisibility(View.GONE);
        }
    }

    private void initializePlayer() {
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), new DefaultTrackSelector(), new DefaultLoadControl());
        String userAgent = Util.getUserAgent(getContext(), getContext().getString(R.string.app_name));
        MediaSource mediaSource = new ExtractorMediaSource
                .Factory(new DefaultDataSourceFactory(getContext(), userAgent))
                .setExtractorsFactory(new DefaultExtractorsFactory())
                .createMediaSource(Uri.parse(mVideoUrl));

        mExoPlayer.prepare(mediaSource);
        mExoPlayer.setPlayWhenReady(false);
        mPlayerView.setPlayer(mExoPlayer);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            releasePlayer();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        if (unbinder != null) {
//            unbinder.unbind();
//        }
        releasePlayer();
    }

    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    void setStep(Step step) {
        this.mStep = step;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(SAVED_INSTANCE_STEP_OBJECT, mStep);
        outState.putBoolean(SAVED_INSTANCE_HAS_PREV, mHasPrev);
        outState.putBoolean(SAVED_INSTANCE_HAS_NEXT, mHasNext);
    }
}
