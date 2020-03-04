package com.example.bakingtime.ui;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class RecipeStepFragment extends Fragment {

    private static final String SAVED_INSTANCE_STEP_OBJECT = "step";
    private Step mStep;
    private String mVideoUrl;
    //    private Unbinder unbinder;
    private ExoPlayer mExoPlayer;
    //    @BindView(R.id.recipe_step_video)
    PlayerView mPlayerView;
    //    @BindView(R.id.recipe_step_description_card)
    CardView mRecipeStepDescriptionCardView;
    //    @BindView(R.id.recipe_step_description)
    TextView mRecipeStepDescription;
    //    @BindView(R.id.recipe_next_prev_step)
    Button mButton;

    public RecipeStepFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(SAVED_INSTANCE_STEP_OBJECT)) {
            mStep = savedInstanceState.getParcelable(SAVED_INSTANCE_STEP_OBJECT);
        }

        if (mStep == null) {
            ((RecipeStepActivity) requireActivity()).closeOnError();
            return null;
        }

        final View rootView = inflater.inflate(R.layout.fragment_recipe_step, container, false);
//        unbinder = ButterKnife.bind(this, rootView);

        mPlayerView = rootView.findViewById(R.id.recipe_step_video);
        mRecipeStepDescriptionCardView = rootView.findViewById(R.id.recipe_step_description_card);
        mRecipeStepDescription = rootView.findViewById(R.id.recipe_step_description);
        mButton = rootView.findViewById(R.id.recipe_next_prev_step);

        mVideoUrl = !mStep.getVideoURL().isEmpty() ? mStep.getVideoURL() : mStep.getThumbnailURL();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mRecipeStepDescriptionCardView.setVisibility(View.GONE);
            mButton.setVisibility(View.GONE);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mPlayerView.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            params.setMargins(0, 0, 0, 0);
            mPlayerView.setLayoutParams(params);

            View decorView = getActivity().getWindow().getDecorView();
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
        } else {
            mRecipeStepDescription.setText(mStep.getDescription());
        }

        initializePlayer();
        return rootView;
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
    }
}
