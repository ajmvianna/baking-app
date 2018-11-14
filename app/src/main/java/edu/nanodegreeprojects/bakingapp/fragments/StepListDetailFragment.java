package edu.nanodegreeprojects.bakingapp.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import edu.nanodegreeprojects.bakingapp.R;
import edu.nanodegreeprojects.bakingapp.adapters.IngredientAdapter;
import edu.nanodegreeprojects.bakingapp.model.Ingredient;
import edu.nanodegreeprojects.bakingapp.model.Recipe;
import edu.nanodegreeprojects.bakingapp.model.Step;

public class StepListDetailFragment extends Fragment implements ExoPlayer.EventListener, IngredientAdapter.IngredientAdapterOnClickHandler {

    private TextView textViewShortDescription, textViewDescription;
    private Step step;
    private Recipe recipe;

    private static String M_TWO_PANELS_TAG = "m_two_panels_tag";
    private boolean mTwoPanels = false;

    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private PlaybackStateCompat.Builder mStateBuilder;
    private MediaSessionCompat mMediaSession;
    private static final String TAG_MEDIA_SESSION = "media-session";

    private RecyclerView recyclerViewIngredients;
    private IngredientAdapter ingredientsAdapter;
    private ImageView ivThumbnail;
    private int NUMBER_OF_COLUMNS = 1;
    private boolean destroyVideoFlag = true;
    private long exoDuration = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_steps_list_detail, container, false);

        if (getArguments() != null) {
            Bundle extras = getArguments();
            step = (Step) extras.get("step");
            recipe = (Recipe) extras.get("recipe");
            mTwoPanels = extras.getBoolean(M_TWO_PANELS_TAG);
            loadComponents(rootView, step, exoDuration);
        }
        if (savedInstanceState != null) {
            Step tempStep = (Step) savedInstanceState.getSerializable("selectedStep");
            Recipe tempRecipe = (Recipe) savedInstanceState.getSerializable("recipe");
            boolean tempMTwoPanels = savedInstanceState.getBoolean(M_TWO_PANELS_TAG);
            exoDuration = savedInstanceState.getLong("exo_duration");

            if (tempStep != null)
                step = tempStep;

            if (tempRecipe != null)
                recipe = tempRecipe;

            mTwoPanels = tempMTwoPanels;
            loadComponents(rootView, step, exoDuration);
        }


        return rootView;
    }

    private void loadComponents(View rootView, Step step, long exoDuration) {
        textViewDescription = rootView.findViewById(R.id.tv_description);
        textViewShortDescription = rootView.findViewById(R.id.tv_short_description);
        mPlayerView = rootView.findViewById(R.id.playerView);
        ivThumbnail = rootView.findViewById(R.id.iv_recipe_thumbnail);

        if (step != null) {
            textViewShortDescription.setText(step.getShortDescription());
            recyclerViewIngredients = rootView.findViewById(R.id.recyclerViewIngredients);

            //Ingredients
            if (step.getId() == -1) {

                GridLayoutManager gridLayoutManager = new GridLayoutManager(rootView.getContext(), NUMBER_OF_COLUMNS);
                recyclerViewIngredients.setLayoutManager(gridLayoutManager);
                ingredientsAdapter = new IngredientAdapter(this);
                recyclerViewIngredients.setAdapter(ingredientsAdapter);
                recyclerViewIngredients.setHasFixedSize(false);
                recyclerViewIngredients.setVisibility(View.VISIBLE);
                ingredientsAdapter.setIngredientData(recipe.getIngredientList());


            } else {
                textViewDescription.setText(step.getDescription());
                if (!step.getVideoURL().equals("")) {
                    mPlayerView.setVisibility(View.VISIBLE);

                    if (exoDuration > 0) {
                        mExoPlayer.seekTo(exoDuration);
                        mExoPlayer.setPlayWhenReady(true);
                    } else {
                        initializeMediaSession();
                        initializePlayer(Uri.parse(step.getVideoURL()));
                    }
                } else {
                    mPlayerView.setVisibility(View.GONE);
                }

                if (!step.getThumbnailURL().equals("")) {
                    Picasso.with(this.getContext()).load(step.getThumbnailURL()).error(R.drawable.ic_error_loading_image).into(ivThumbnail);
                } else {
                    ivThumbnail.setVisibility(View.GONE);
                }

                recyclerViewIngredients.setVisibility(View.GONE);
            }
        }


    }


    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), null);
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), getString(R.string.app_name)), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);

        }
    }

    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(getContext(), TAG_MEDIA_SESSION);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());

        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);

    }

    private void releasePlayer() {
        if (step != null && !step.getVideoURL().equals("") && mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (step != null && !step.getVideoURL().equals("") && destroyVideoFlag)
            releasePlayer();
    }

    @Override
    public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

    @Override
    public void onClick(Ingredient ingredient) {

    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mExoPlayer != null)
            outState.putLong("exo_duration", mExoPlayer.getCurrentPosition());
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            mExoPlayer.setPlayWhenReady(false);
            mExoPlayer.getPlaybackState();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mExoPlayer != null) {
            mExoPlayer.setPlayWhenReady(true);
            mExoPlayer.getPlaybackState();
        }
    }
}
