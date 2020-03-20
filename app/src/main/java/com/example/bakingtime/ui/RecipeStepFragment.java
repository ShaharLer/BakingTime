package com.example.bakingtime.ui;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bakingtime.R;
import com.example.bakingtime.database.Step;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class RecipeStepFragment extends Fragment {

    private static final String SAVED_INSTANCE_STEP_OBJECT = "step";
    private static final String SAVED_INSTANCE_HAS_PREV = "prev";
    private static final String SAVED_INSTANCE_HAS_NEXT = "next";
    private static final String SAVED_INSTANCE_VIDEO_WINDOW = "window";
    private static final String SAVED_INSTANCE_VIDEO_START_POSITION = "position";
    private static final String SAVED_INSTANCE_VIDEO_AUTO_PLAY = "auto_play";

    private Step mStep;
    private String mVideoUrl;
    private boolean mHasPrev;
    private boolean mHasNext;
    private boolean mStartAutoPlay;
    private int mStartWindow;
    private long mStartPosition;

    //    private Unbinder unbinder;
    //    private ExoPlayer mPlayer;
    private SimpleExoPlayer mPlayer;
    //    @BindView(R.id.recipe_step_video)
    private SimpleExoPlayerView mPlayerView;
    private ImageView mNoVideoIv;

    //    @BindView(R.id.recipe_step_description)
    private FrameLayout mStepDescriptionFrameLayout;
    private RelativeLayout mRecipeStepButtonsLayout;

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
        if (savedInstanceState != null) {
            mStep = savedInstanceState.getParcelable(SAVED_INSTANCE_STEP_OBJECT);
            mHasPrev = savedInstanceState.getBoolean(SAVED_INSTANCE_HAS_PREV);
            mHasNext = savedInstanceState.getBoolean(SAVED_INSTANCE_HAS_NEXT);
            mStartAutoPlay = savedInstanceState.getBoolean(SAVED_INSTANCE_VIDEO_AUTO_PLAY);
            mStartWindow = savedInstanceState.getInt(SAVED_INSTANCE_VIDEO_WINDOW);
            mStartPosition = savedInstanceState.getLong(SAVED_INSTANCE_VIDEO_START_POSITION);
        } else {
            mStartWindow = C.INDEX_UNSET;
        }

        if (mStep == null) {
            // TODO act different whether on tablet or not
            ((RecipeStepActivity) requireActivity()).closeOnError();
            return null;
        }
        mVideoUrl = mStep.getVideoURL().isEmpty() ? mStep.getThumbnailURL() : mStep.getVideoURL();

        View rootView = inflater.inflate(R.layout.fragment_recipe_step, container, false);
        mPlayerView = rootView.findViewById(R.id.recipe_step_video);
        mNoVideoIv = rootView.findViewById(R.id.no_video_available);
        mStepDescriptionFrameLayout = rootView.findViewById(R.id.recipe_step_description_frame_layout);
        mRecipeStepButtonsLayout = rootView.findViewById(R.id.recipe_steps_buttons_layout);
        ((TextView) rootView.findViewById(R.id.recipe_step_description_tv)).setText(mStep.getDescription());
        setButtonClickListener(rootView.findViewById(R.id.recipe_prev_step), mHasPrev, false);
        setButtonClickListener(rootView.findViewById(R.id.recipe_next_step), mHasNext, true);
        updateScreen(getResources().getConfiguration());
        return rootView;
    }

    private void setButtonClickListener(Button button, boolean showButton, boolean isNext) {
        if (showButton) {
            button.setOnClickListener(v -> ((RecipeStepActivity) requireActivity()).onPrevNextStepsClicked(isNext));
        } else {
            button.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            executeInitPlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || mPlayer == null) {
            executeInitPlayer();
        }
    }

    private void executeInitPlayer() {
        if (mVideoUrl != null && !mVideoUrl.isEmpty()) {
            showVideo();
            initializePlayer(mVideoUrl);
            if (mPlayerView != null) {
                mPlayerView.onResume();
            }
        } else {
            showVideoError();
        }
    }

    private void showVideo() {
        mNoVideoIv.setVisibility(View.GONE);
        mPlayerView.setVisibility(View.VISIBLE);
    }

    private void showVideoError() {
        mPlayerView.setVisibility(View.GONE);
        mNoVideoIv.setVisibility(View.VISIBLE);
    }

    private void initializePlayer(String videoUrl) {
        mPlayer = ExoPlayerFactory.newSimpleInstance(Objects.requireNonNull(getContext()),
                new DefaultTrackSelector(), new DefaultLoadControl());
        String userAgent = Util.getUserAgent(getContext(), getContext().getString(R.string.app_name));
        MediaSource mediaSource = new ExtractorMediaSource
                .Factory(new DefaultDataSourceFactory(getContext(), userAgent))
                .setExtractorsFactory(new DefaultExtractorsFactory())
                .createMediaSource(Uri.parse(videoUrl));
        boolean haveStartPosition = mStartWindow != C.INDEX_UNSET;
        if (haveStartPosition) {
            mPlayer.seekTo(mStartWindow, mStartPosition);
        }
        mPlayer.prepare(mediaSource, !haveStartPosition, false);
        mPlayer.setPlayWhenReady(mStartAutoPlay);
        mPlayerView.setPlayer(mPlayer);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            executeRealeasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            executeRealeasePlayer();
        }
    }

    private void executeRealeasePlayer() {
        if (mPlayerView != null) {
            mPlayerView.onPause();
        }
        releasePlayer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        if (unbinder != null) {
//            unbinder.unbind();
//        }

        /*
        if (!Objects.requireNonNull(getActivity()).isChangingConfigurations()) {
            releasePlayer();
        }
         */
    }

    private void releasePlayer() {
        if (mPlayer != null) {
            updateStartPosition();
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(SAVED_INSTANCE_STEP_OBJECT, mStep);
        outState.putBoolean(SAVED_INSTANCE_HAS_PREV, mHasPrev);
        outState.putBoolean(SAVED_INSTANCE_HAS_NEXT, mHasNext);
        updateStartPosition();
        outState.putBoolean(SAVED_INSTANCE_VIDEO_AUTO_PLAY, mStartAutoPlay);
        outState.putInt(SAVED_INSTANCE_VIDEO_WINDOW, mStartWindow);
        outState.putLong(SAVED_INSTANCE_VIDEO_START_POSITION, mStartPosition);
    }

    private void updateStartPosition() {
        if (mPlayer != null) {
            mStartAutoPlay = false;
            mStartWindow = mPlayer.getCurrentWindowIndex();
            mStartPosition = Math.max(0, mPlayer.getContentPosition());
        }
    }

    // TODO check why doesn't get called in tablet mode (master-detail flow)
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateScreen(newConfig);
    }

    private void updateScreen(Configuration config) {
        int smallestScreenWidthDp = config.smallestScreenWidthDp;
        int orientation = config.orientation;

        if (smallestScreenWidthDp >= 600) {
            mRecipeStepButtonsLayout.setVisibility(View.GONE);
        } else {
            View decorView = Objects.requireNonNull(getActivity()).getWindow().getDecorView();
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                Toolbar toolbar = getActivity().findViewById(R.id.my_toolbar);
                toolbar.setVisibility(View.GONE);
                mStepDescriptionFrameLayout.setVisibility(View.GONE);
                mRecipeStepButtonsLayout.setVisibility(View.GONE);
                decorView.setSystemUiVisibility(
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

                                // Hide the nav bar and status bar
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
//                                | View.SYSTEM_UI_FLAG_LOW_PROFILE
                );
            } else {
                decorView.setSystemUiVisibility(0);
                Toolbar toolbar = getActivity().findViewById(R.id.my_toolbar);
                toolbar.setVisibility(View.VISIBLE);
                mStepDescriptionFrameLayout.setVisibility(View.VISIBLE);
                mRecipeStepButtonsLayout.setVisibility(View.VISIBLE);
            }
        }
    }
}
