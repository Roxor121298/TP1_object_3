package com.example.vrai_tp1;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import java.util.Vector;

public class Ecouteur implements AdapterView.OnItemClickListener{

    private Context context;
    private Vector<Musique> musicData;

    // Constructor now takes a Vector of Musique objects
    public Ecouteur(Context context, Vector<Musique> musicData) {
        this.context = context;
        this.musicData = musicData;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Retrieve the clicked Musique object
        Musique selectedMusic = musicData.get(position);

        // Create an Intent to start the new activity
        Intent intent = new Intent(context, JouerActivity_test.class);

        // Pass the selected Musique data to the next activity using intent extras
        intent.putExtra("title", selectedMusic.getTitle());
        intent.putExtra("album", selectedMusic.getAlbum());
        intent.putExtra("artist", selectedMusic.getArtist());
        intent.putExtra("genre", selectedMusic.getGenre());
        intent.putExtra("image", selectedMusic.getImage());  // URL of the image
        intent.putExtra("source", selectedMusic.getSource());

        // Start the new activity
        context.startActivity(intent);
    }


}
