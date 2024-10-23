package com.example.vrai_tp1;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.UrlRewriter;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    ListView liste;
    Vector<Hashtable<String, Object>> vect = new Vector<Hashtable<String, Object>>();
    Hashtable<String, Object> temp = new Hashtable<>();

    EcouteurTouch ec;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        liste = findViewById(R.id.liste);
    }

    //ui = findViewById(R.id.ui);

   RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
    String url = "https://api.npoint.io/d4c29479e010376e6847?meta=false";





    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //pseudo code pour aider a complété
                    JSONArray tab = null;
                    try {
                        tab = (JSONArray) response.get("music");
                        JSONObject le_premier = tab.getJSONObject(2);
                        String prix = (String) le_premier.get("album");
                        //ui.setText("Response: " + prix);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    //ui.setText("Response: " + response.toString());
                    //Toast.makeText(getApplicationContext(), "Response: " + response.toString(), Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO: Handle error
                }
            });


    class EcouteurTouch implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Hashtable<String, Object> rep  = vect.get(position);
            String nom = (String) rep.get("nom");
            Toast.makeText(getApplicationContext(),nom, Toast.LENGTH_LONG).show();
        }

    }
}