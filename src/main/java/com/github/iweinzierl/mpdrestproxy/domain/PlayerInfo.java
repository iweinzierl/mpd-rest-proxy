package com.github.iweinzierl.mpdrestproxy.domain;

import java.util.Collection;

public class PlayerInfo {

    private PlayerStatus status;

    private int volume;

    private Song currentSong;

    private Collection<Song> playlist;

    private long elapsedTime;

    private long totalTime;

    public PlayerStatus getStatus() {
        return status;
    }

    public void setStatus(PlayerStatus status) {
        this.status = status;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public Song getCurrentSong() {
        return currentSong;
    }

    public void setCurrentSong(Song currentSong) {
        this.currentSong = currentSong;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public Collection<Song> getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Collection<Song> playlist) {
        this.playlist = playlist;
    }
}
