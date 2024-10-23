package com.example.vrai_tp1;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class JouerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jouer);

        // Retrieve the data passed from the previous activity
        String title = getIntent().getStringExtra("title");
        String album = getIntent().getStringExtra("album");
        String artist = getIntent().getStringExtra("artist");
        String genre = getIntent().getStringExtra("genre");
        String imageUrl = getIntent().getStringExtra("image");

        // Find the views by ID
        TextView titleView = findViewById(R.id.titleView);
        TextView albumView = findViewById(R.id.albumView);
        TextView artistView = findViewById(R.id.artistView);
        TextView genreView = findViewById(R.id.genreView);
        ImageView imageView = findViewById(R.id.imageView);

        // Set the retrieved data to the respective views
        titleView.setText(title);
        albumView.setText(album);
        artistView.setText(artist);
        genreView.setText(genre);

        // Load the image using Glide
        Glide.with(this).load(imageUrl).into(imageView);
    }
}
