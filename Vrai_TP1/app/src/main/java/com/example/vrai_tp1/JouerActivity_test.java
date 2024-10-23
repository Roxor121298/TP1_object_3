package com.example.vrai_tp1;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;
import androidx.media3.common.Player;
import android.os.Handler;

public class JouerActivity_test extends AppCompatActivity {

    private PlayerView playerView;
    private ExoPlayer exoPlayer;
    private ProgressBar progressBar;
    private Button playButton, pauseButton, forwardButton, backwardButton, retour;
    private Handler progressHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jouer);

        // Retrieve data passed from the previous activity
        String title = getIntent().getStringExtra("title");
        String album = getIntent().getStringExtra("album");
        String artist = getIntent().getStringExtra("artist");
        String genre = getIntent().getStringExtra("genre");
        String imageUrl = getIntent().getStringExtra("image");
        String sourceUrl = getIntent().getStringExtra("source");

        // Find views
        TextView titleView = findViewById(R.id.titleView);
        TextView albumView = findViewById(R.id.albumView);
        TextView artistView = findViewById(R.id.artistView);
        TextView genreView = findViewById(R.id.genreView);
        ImageView imageView = findViewById(R.id.imageView);
        playerView = findViewById(R.id.playerView);
        progressBar = findViewById(R.id.progressBar);
        playButton = findViewById(R.id.playButton);
        pauseButton = findViewById(R.id.pauseButton);
        forwardButton = findViewById(R.id.forwardButton);
        backwardButton = findViewById(R.id.backwardButton);
        retour = findViewById(R.id.retour);

        // Set the media metadata in views
        titleView.setText(title);
        albumView.setText(album);
        artistView.setText(artist);
        genreView.setText(genre);
        Glide.with(this).load(imageUrl).into(imageView);

        // Set OnClickListener for the return button
        retour.setOnClickListener(new EcouteurButton());

        // Initialize the ExoPlayer
        initializePlayer(sourceUrl);

        // Play button listener
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exoPlayer.play();
                updateProgressBar(); // Start updating progress
            }
        });

        // Pause button listener
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exoPlayer.pause();
                progressHandler.removeCallbacks(updateProgressAction); // Stop progress updates
            }
        });

        // Forward button listener (move forward 10 seconds)
        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentPosition = exoPlayer.getCurrentPosition();
                long duration = exoPlayer.getDuration();
                // Move forward 10 seconds, but don't exceed the media duration
                exoPlayer.seekTo(Math.min(currentPosition + 10000, duration));
            }
        });

        // Backward button listener (move backward 10 seconds)
        backwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentPosition = exoPlayer.getCurrentPosition();
                // Move backward 10 seconds, but don't go below 0
                exoPlayer.seekTo(Math.max(currentPosition - 10000, 0));
            }
        });
    }

    // Initialize ExoPlayer and link it to the PlayerView
    private void initializePlayer(String sourceUrl) {
        if (sourceUrl == null || sourceUrl.isEmpty()) {
            Log.e("JouerActivity_test", "Invalid source URL");
            return;
        }

        // Create the ExoPlayer instance using the Builder pattern
        exoPlayer = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(exoPlayer);
        playerView.setUseController(false); // Disable default controller

        // Create a MediaItem using the source URL (MP3 file)
        MediaItem mediaItem = MediaItem.fromUri(sourceUrl);

        // Add the MediaItem to the ExoPlayer
        exoPlayer.setMediaItem(mediaItem);

        // Prepare the player and pause it initially
        exoPlayer.prepare();
        exoPlayer.pause();

        // Set up the progress bar to match the media's duration
        exoPlayer.addListener(new Player.Listener() {
            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                if (isPlaying) {
                    progressBar.setMax((int) exoPlayer.getDuration()); // Set progress bar max to media duration
                    updateProgressBar(); // Start updating progress
                }
            }
        });
    }

    // Update the progress bar as the media plays
    private void updateProgressBar() {
        progressHandler.postDelayed(updateProgressAction, 1000); // Update every second
    }

    // Runnable to update progress bar
    private final Runnable updateProgressAction = new Runnable() {
        @Override
        public void run() {
            if (exoPlayer != null) {
                progressBar.setProgress((int) exoPlayer.getCurrentPosition()); // Update progress bar with current position
                progressHandler.postDelayed(this, 1000); // Keep updating every second
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
        progressHandler.removeCallbacks(updateProgressAction); // Stop progress updates when destroyed
    }

    private class EcouteurButton implements View.OnClickListener {
        @Override
        public void onClick(View event) {
            if (event == retour) {
                Log.d("JouerActivity_test", "Retour button clicked");
                finish(); // Return to the previous activity
            }
        }
    }
}


