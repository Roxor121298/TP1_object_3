package com.example.vrai_tp1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MusiqueAdapter extends ArrayAdapter<Musique> {
    private Context context;
    private List<Musique> musiqueList;

    public MusiqueAdapter(Context context, List<Musique> musiqueList) {
        super(context, R.layout.une_musique, musiqueList);
        this.context = context;
        this.musiqueList = musiqueList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.une_musique, parent, false);
        }

        Musique musique = musiqueList.get(position);
        TextView titleView = convertView.findViewById(R.id.titre);
        TextView albumView = convertView.findViewById(R.id.album);
        TextView artistView = convertView.findViewById(R.id.artist);
        TextView genreView = convertView.findViewById(R.id.genre);
        ImageView imageView = convertView.findViewById(R.id.image);

        titleView.setText(musique.getTitle());
        albumView.setText(musique.getAlbum());
        artistView.setText(musique.getArtist());
        genreView.setText(musique.getGenre());

        Glide.with(context).load(musique.getImage()).into(imageView);
        return convertView;
    }
}
