package com.example.vrai_tp1;

import static android.content.Intent.getIntent;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Ecouteur implements AdapterView.OnItemClickListener{

    private Context context;
    private Vector<Musique> musicData;
    public Ecouteur(Context context, Vector<Musique> musicData) {
        this.context = context;
        this.musicData = musicData;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Musique selectedMusic = musicData.get(position);

        // test car jai eux des bugs ici
        if (selectedMusic == null) {
            Log.e("Ecouteur", "Selected music is null");
            return;
        }

        // avant je passait les intent une a un et je les bindait dans la classe JeuActivity_test
        System.out.println(selectedMusic);
        // Create an Intent to start the new activity
        Intent intent = new Intent(context, JouerActivity_test.class);
        // Pass the selected Musique data to the next activity using intent extras
        intent.putExtra("title", selectedMusic.getTitle());
        intent.putExtra("album", selectedMusic.getAlbum());
        intent.putExtra("artist", selectedMusic.getArtist());
        intent.putExtra("genre", selectedMusic.getGenre());
        intent.putExtra("image", selectedMusic.getImage());  // URL of the image
        intent.putExtra("source", selectedMusic.getSource());
        intent.putExtra("currentIncrement",selectedMusic.getIncrement());

        // Pass the entire list of Musique objects and the current song index
        intent.putExtra("musiqueList", new ArrayList<>(musicData));


        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // ce qui plus important est ici
        // J'ai laisser l'écouteur comme il était avant mais maintenant j'utilise jsute un index currentSongIndex et LEJSON

        intent.putExtra("currentSongIndex", position);

        // Start the new activity
        context.startActivity(intent);
    }



}
