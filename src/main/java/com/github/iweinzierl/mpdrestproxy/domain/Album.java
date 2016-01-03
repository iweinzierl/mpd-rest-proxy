package com.github.iweinzierl.mpdrestproxy.domain;

public class Album {

    private Artist artist;

    private String name;

    public Album() {
    }

    public Album(Artist artist, String name) {
        this.artist = artist;
        this.name = name;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
