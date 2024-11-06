package com.example.vrai_tp1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LeJSON implements Serializable {
    private static LeJSON instance;
    private List<Musique> musicData;

    private LeJSON() {
        this.musicData = new ArrayList<>();
    }

    public static synchronized LeJSON getInstance() {
        if (instance == null) {
            instance = new LeJSON();
        }
        return instance;
    }

    public List<Musique> getMusicData() {
        return musicData;
    }
    public void setMusicData(List<Musique> musicData) {
        this.musicData = musicData;
    }
    public void addMusique(Musique musique) {
        this.musicData.add(musique);
    }
}
