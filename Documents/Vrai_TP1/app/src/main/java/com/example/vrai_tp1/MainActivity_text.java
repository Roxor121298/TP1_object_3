package com.example.vrai_tp1;

import android.os.Bundle;
import android.widget.ImageView;
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
    // Adresse originale
    //String url = "https://api.npoint.io/d4c29479e010376e6847?meta=false";

    // Adresse supplémentaire : https://api.jsonbin.io/v3/b/6723b430e41b4d34e44bfa92?meta=false
    String url = "https://api.jsonbin.io/v3/b/6723b430e41b4d34e44bfa92?meta=false";


    // représente la liste de musique (le fichier JSON)
    LeJSON leJSON = LeJSON.getInstance(); // Get the singleton instance
   //List<Musique> musiqueListe = leJSON.getMusicData();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_text);

        liste = findViewById(R.id.liste);

        // récupéré les infos du serveur

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

    // methode setup LeJSOn et le ListView
    private void parseJsonAndAddToList(JSONObject response) {
        // Parse the JSON response and populate LeJSON's musicData
        Gson gson = new Gson();
        JsonObject jsonResponse = gson.fromJson(response.toString(), JsonObject.class);
        JsonArray musicArray = jsonResponse.getAsJsonArray("music");

        // Clear existing data in LeJSON
        List<Musique> musiqueList = leJSON.getMusicData();
        musiqueList.clear();

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

        // Set the updated list back to LeJSON
        leJSON.setMusicData(musiqueList);


        // set on clickListenner poru changer d'activité
        liste.setOnItemClickListener(new Ecouteur(this, new Vector<>(musiqueList)));

        //
        createMusiqueAdapterAndSetToListView();
    }


    // fonction pour setup le view dans la classe MusiqueAdapter
    private void createMusiqueAdapterAndSetToListView() {
        MusiqueAdapter adapter = new MusiqueAdapter(this, leJSON.getMusicData());
        liste.setAdapter(adapter);
    }

}




