package com.example.vrai_tp1;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
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

import java.util.List;

public class JouerActivity_test extends AppCompatActivity {
    // Instance of LeJSON to access music data
    LeJSON leJSON = LeJSON.getInstance();
    List<Musique> musiqueList = leJSON.getMusicData();

    private int currentSongIndex;

    // UI elements
    private TextView titleView, albumView, artistView, genreView;
    private ImageView imageView;
    private PlayerView playerView;
    private ExoPlayer exoPlayer;
    private ProgressBar progressBar;
    private ImageButton playButton, pauseButton, forwardButton, backwardButton, previousButton, nextButton, retour;
    private Handler progressHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jouer);

        currentSongIndex = getIntent().getIntExtra("currentSongIndex", 0);
        if (musiqueList == null || musiqueList.isEmpty()) {
            Log.e("JouerActivity_test", "musiqueList is null or empty");
            return;
        }

        titleView = findViewById(R.id.titleView);
        albumView = findViewById(R.id.albumView);
        artistView = findViewById(R.id.artistView);
        genreView = findViewById(R.id.genreView);
        imageView = findViewById(R.id.imageView);
        playerView = findViewById(R.id.playerView);
        progressBar = findViewById(R.id.progressBar);
        playButton = findViewById(R.id.playButton);
        pauseButton = findViewById(R.id.pauseButton);
        forwardButton = findViewById(R.id.forwardButton);
        backwardButton = findViewById(R.id.backwardButton);
        previousButton = findViewById(R.id.previousButton);
        nextButton = findViewById(R.id.nextButton);
        retour = findViewById(R.id.retour);

        setupViewsAndPlayer(currentSongIndex);

        retour.setOnClickListener(new EcouteurButton());


        playButton.setOnClickListener(v -> {
            exoPlayer.play();
            updateProgressBar();
        });

        pauseButton.setOnClickListener(v -> {
            exoPlayer.pause();
            progressHandler.removeCallbacks(updateProgressAction);
        });

        forwardButton.setOnClickListener(v -> {
            long currentPosition = exoPlayer.getCurrentPosition();
            long duration = exoPlayer.getDuration();
            exoPlayer.seekTo(Math.min(currentPosition + 10000, duration));
        });

        backwardButton.setOnClickListener(v -> {
            long currentPosition = exoPlayer.getCurrentPosition();
            exoPlayer.seekTo(Math.max(currentPosition - 10000, 0));
        });

        nextButton.setOnClickListener(v -> {
            if (currentSongIndex < musiqueList.size() - 1) {
                currentSongIndex++;
                setupViewsAndPlayer(currentSongIndex);
            }
        });

        previousButton.setOnClickListener(v -> {
            if (currentSongIndex > 0) {
                currentSongIndex--;
                setupViewsAndPlayer(currentSongIndex);
            }
        });

    }

    private void setupViewsAndPlayer(int index) {
        Musique currentSong = musiqueList.get(index);


        titleView.setText(currentSong.getTitle());
        albumView.setText(currentSong.getAlbum());
        artistView.setText(currentSong.getArtist());
        genreView.setText(currentSong.getGenre());
        Glide.with(this).load(currentSong.getImage()).into(imageView);


        if (exoPlayer == null) {
            exoPlayer = new ExoPlayer.Builder(this).build();
            playerView.setPlayer(exoPlayer);
            playerView.setUseController(false);
        }


        exoPlayer.setMediaItem(MediaItem.fromUri(currentSong.getSource()));
        exoPlayer.prepare();
        exoPlayer.pause();

    }

    private void updateProgressBar() {
        progressBar.setMax((int) exoPlayer.getDuration());
        progressHandler.postDelayed(updateProgressAction, 1000);
    }

    // Runnable to update progress bar
    private final Runnable updateProgressAction = new Runnable() {
        @Override
        public void run() {
            if (exoPlayer != null) {
                progressBar.setProgress((int) exoPlayer.getCurrentPosition()); // Update progress bar with current position
                progressHandler.postDelayed(this, 1000);
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
        progressHandler.removeCallbacks(updateProgressAction);
    }

    private class EcouteurButton implements View.OnClickListener {
        @Override
        public void onClick(View event) {
            if (event == retour) {
                Log.d("JouerActivity_test", "Retour button clicked");
                finish();
            }
        }
    }
}
