package com.github.iweinzierl.mpdrestproxy.helper;

import com.github.iweinzierl.mpdrestproxy.domain.Album;
import com.github.iweinzierl.mpdrestproxy.domain.Artist;
import com.github.iweinzierl.mpdrestproxy.domain.Song;
import org.bff.javampd.album.MPDAlbum;
import org.bff.javampd.song.MPDSong;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public final class Converter {

    private Converter() {
    }

    public static List<Album> convertAlbums(Collection<MPDAlbum> albums) {
        return albums.parallelStream().map(
                album -> {
                    Artist artist = new Artist(album.getArtistName());
                    return new Album(artist, album.getName());
                }
        ).collect(Collectors.toList());
    }

    public static List<Song> convertSongs(Collection<MPDSong> songs) {
        return songs.parallelStream().map(Converter::convertSong).collect(Collectors.toList());
    }

    public static Song convertSong(MPDSong song) {
        if (song == null) {
            return null;
        }

        Artist artist = new Artist(song.getArtistName());

        Song transformedSong = new Song();
        transformedSong.setTitle(song.getTitle());
        transformedSong.setFile(song.getFile());
        transformedSong.setArtist(artist);
        transformedSong.setAlbum(new Album(artist, song.getAlbumName()));
        transformedSong.setGenre(song.getGenre());
        transformedSong.setComment(song.getComment());
        transformedSong.setYear(song.getYear());
        transformedSong.setDiscNumber(song.getDiscNumber());
        transformedSong.setLength(song.getLength());
        transformedSong.setTrack(song.getTrack());
        transformedSong.setPosition(song.getPosition());
        transformedSong.setId(song.getId());

        return transformedSong;
    }

    public static List<MPDSong> convertToMpdSongs(Collection<Song> songs) {
        return songs.parallelStream().map(
                song -> {
                    MPDSong mpdSong = new MPDSong(song.getFile(), song.getTitle());
                    mpdSong.setId(song.getId());
                    mpdSong.setAlbumName(song.getAlbum().getName());
                    mpdSong.setArtistName(song.getArtist().getName());
                    mpdSong.setComment(song.getComment());
                    mpdSong.setDiscNumber(song.getDiscNumber());
                    mpdSong.setGenre(song.getGenre());
                    mpdSong.setLength(song.getLength());
                    mpdSong.setPosition(song.getPosition());
                    mpdSong.setTrack(song.getTrack());
                    mpdSong.setYear(song.getYear());

                    return mpdSong;
                }
        ).collect(Collectors.toList());
    }
}
