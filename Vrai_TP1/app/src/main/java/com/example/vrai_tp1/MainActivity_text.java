package com.example.vrai_tp1;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

public class MainActivity_text extends AppCompatActivity {

    ListView liste;
    List<Musique> musiqueList = new ArrayList<>();
    String url = "https://api.npoint.io/d4c29479e010376e6847?meta=false";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        liste = findViewById(R.id.liste);


        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseJsonAndAddToList(response); // methode pour ajouter au lieux de coder a l'intérieur
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Erreur lors de la récupération des données", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(jsonObjectRequest);
    }



    private void parseJsonAndAddToList(JSONObject response) {
        Gson gson = new Gson();
        JsonObject jsonResponse = gson.fromJson(response.toString(), JsonObject.class);
        JsonArray musicArray = jsonResponse.getAsJsonArray("music");

        for (int i = 0; i < musicArray.size(); i++) {
            JsonObject musicObject = musicArray.get(i).getAsJsonObject();

            Musique musique = new Musique.Builder()
                    .setId(musicObject.get("id").getAsString())
                    .setSite(musicObject.get("site").getAsString())
                    .setAlbum(musicObject.get("album").getAsString())
                    .setGenre(musicObject.get("genre").getAsString())
                    .setImage(musicObject.get("image").getAsString())
                    .setTitle(musicObject.get("title").getAsString())
                    .setArtist(musicObject.get("artist").getAsString())
                    .setSource(musicObject.get("source").getAsString())
                    .setDuration(musicObject.get("duration").getAsInt())
                    .setTrackNumber(musicObject.get("trackNumber").getAsInt())
                    .setTotalTrackCount(musicObject.get("totalTrackCount").getAsInt())
                    .build();

            musiqueList.add(musique);
        }

        // Set the OnItemClickListener after the musiqueList is populated
        liste.setOnItemClickListener(new Ecouteur(this, new Vector<>(musiqueList)));

        // Adapter to display the Musique objects in the ListView
        List<HashMap<String, String>> displayList = new ArrayList<>();
        for (Musique musique : musiqueList) {
            HashMap<String, String> map = new HashMap<>();
            map.put("title", musique.getTitle());
            map.put("album", musique.getAlbum());
            map.put("artist", musique.getArtist());
            map.put("genre", musique.getGenre());
            map.put("image", musique.getImage()); // URL for image
            displayList.add(map);
        }

        // Use the new method to create the adapter
        SimpleAdapter adapter = createMusiqueAdapter(displayList);
        liste.setAdapter(adapter);
    }

    // Move this method outside of any other method
    private SimpleAdapter createMusiqueAdapter(List<HashMap<String, String>> displayList) {
        return new SimpleAdapter(this, displayList, R.layout.une_musique,
                new String[]{"title", "album", "artist", "genre", "image"},
                new int[]{R.id.titre, R.id.album, R.id.artist, R.id.genre, R.id.image}) {

            @Override
            public void setViewImage(android.widget.ImageView v, String value) {
                // Use Glide to load images dynamically
                Glide.with(MainActivity_text.this).load(value).into(v);
            }
        };
    }
}
