package com.example.vrai_tp1;

public class Musique {
    private final String id;
    private final String site;
    private final String album;
    private final String genre;
    private final String image;
    private final String title;
    private final String artist;
    private final String source;
    private final int duration;
    private final int trackNumber;
    private final int totalTrackCount;

    // Private constructor to enforce object creation via the Builder
    private Musique(Builder builder) {
        this.id = builder.id;
        this.site = builder.site;
        this.album = builder.album;
        this.genre = builder.genre;
        this.image = builder.image;
        this.title = builder.title;
        this.artist = builder.artist;
        this.source = builder.source;
        this.duration = builder.duration;
        this.trackNumber = builder.trackNumber;
        this.totalTrackCount = builder.totalTrackCount;
    }

    // Static nested Builder class
    public static class Builder {
        private String id;
        private String site;
        private String album;
        private String genre;
        private String image;
        private String title;
        private String artist;
        private String source;
        private int duration;
        private int trackNumber;
        private int totalTrackCount;

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setSite(String site) {
            this.site = site;
            return this;
        }

        public Builder setAlbum(String album) {
            this.album = album;
            return this;
        }

        public Builder setGenre(String genre) {
            this.genre = genre;
            return this;
        }

        public Builder setImage(String image) {
            this.image = image;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setArtist(String artist) {
            this.artist = artist;
            return this;
        }

        public Builder setSource(String source) {
            this.source = source;
            return this;
        }

        public Builder setDuration(int duration) {
            this.duration = duration;
            return this;
        }

        public Builder setTrackNumber(int trackNumber) {
            this.trackNumber = trackNumber;
            return this;
        }

        public Builder setTotalTrackCount(int totalTrackCount) {
            this.totalTrackCount = totalTrackCount;
            return this;
        }

        // Method to build and return the final Musique object
        public Musique build() {
            return new Musique(this);
        }
    }

    // Getters for the Musique class fields
    public String getId() { return id; }
    public String getSite() { return site; }
    public String getAlbum() { return album; }
    public String getGenre() { return genre; }
    public String getImage() { return image; }
    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public String getSource() { return source; }
    public int getDuration() { return duration; }
    public int getTrackNumber() { return trackNumber; }
    public int getTotalTrackCount() { return totalTrackCount; }
}
