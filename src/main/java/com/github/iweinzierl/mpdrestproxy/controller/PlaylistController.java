package com.github.iweinzierl.mpdrestproxy.controller;

import com.github.iweinzierl.mpdrestproxy.domain.Playlist;
import com.github.iweinzierl.mpdrestproxy.helper.Converter;
import com.github.iweinzierl.mpdrestproxy.helper.MPDBean;
import org.bff.javampd.playlist.PlaylistDatabase;
import org.bff.javampd.song.MPDSong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
public class PlaylistController {

    public static final String CURRENT = "current";

    private static final Logger LOG = LoggerFactory.getLogger(PlaylistController.class);

    @Autowired
    private MPDBean mpd;

    @RequestMapping(path = "/api/playlist", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Playlist>> list() {
        final PlaylistDatabase playlistDatabase = mpd.getMpd().getMusicDatabase().getPlaylistDatabase();
        final Collection<String> playlists = playlistDatabase.listPlaylists();

        if (playlists == null || playlists.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(
                playlists.parallelStream().map(playlistName -> {
                    Collection<MPDSong> songs = playlistDatabase.listPlaylistSongs(playlistName);

                    Playlist playlist = new Playlist(playlistName);
                    playlist.setSongs(Converter.convertSongs(songs));

                    return playlist;
                })
                        .collect(Collectors.toList()));
    }

    @RequestMapping(path = "/api/playlist", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Playlist> addPlaylist(@RequestBody Playlist playlist) {
        if (CURRENT.equalsIgnoreCase(playlist.getName())) {
            org.bff.javampd.playlist.Playlist mpdPlaylist = mpd.getMpd().getPlaylist();
            mpdPlaylist.clearPlaylist();
            mpdPlaylist.addSongs(Converter.convertToMpdSongs(playlist.getSongs()));

            return ResponseEntity.ok(playlist);
        } else {
            LOG.warn("Adding new playlist not implemented yet!");
            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        }
    }

    @RequestMapping(path = "/api/playlist/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Playlist> getPlaylist(@PathVariable("name") String playlistName) {
        Playlist playlist = CURRENT.equalsIgnoreCase(playlistName)
                ? getCurrentPlaylist()
                : getPlaylistByName(playlistName);

        if (playlist == null) {
            LOG.info("Playlist not found: {}", playlistName);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(playlist);
    }

    @RequestMapping(path = "/api/playlist/{name}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deletePlaylist(@PathVariable("name") String playlistName) {
        if (CURRENT.equalsIgnoreCase(playlistName)) {
            mpd.getMpd().getPlaylist().clearPlaylist();
            return ResponseEntity.ok(null);
        } else {
            LOG.warn("Erasing of playlist not implemented yet!");
            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        }
    }

    private Playlist getCurrentPlaylist() {
        org.bff.javampd.playlist.Playlist playlist = mpd.getMpd().getPlaylist();

        Playlist result = new Playlist(CURRENT);
        result.setSongs(Converter.convertSongs(playlist.getSongList()));

        return result;
    }

    private Playlist getPlaylistByName(String playlistName) {
        Collection<MPDSong> songs = mpd.getMpd()
                .getMusicDatabase()
                .getPlaylistDatabase()
                .listPlaylistSongs(playlistName);

        if (songs == null || songs.isEmpty()) {
            LOG.info("No songs found for playlist: {}", playlistName);
            return null;
        }

        Playlist playlist = new Playlist(playlistName);
        playlist.setSongs(Converter.convertSongs(songs));

        return playlist;
    }
}
