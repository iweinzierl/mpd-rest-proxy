package com.github.iweinzierl.mpdrestproxy.controller;

import com.github.iweinzierl.mpdrestproxy.domain.Album;
import com.github.iweinzierl.mpdrestproxy.domain.Song;
import com.github.iweinzierl.mpdrestproxy.helper.Converter;
import com.github.iweinzierl.mpdrestproxy.helper.MPDBean;
import com.google.common.base.Strings;
import org.bff.javampd.album.AlbumDatabase;
import org.bff.javampd.album.MPDAlbum;
import org.bff.javampd.artist.MPDArtist;
import org.bff.javampd.song.MPDSong;
import org.bff.javampd.song.SongDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
public class AlbumController {

    private static final Logger LOG = LoggerFactory.getLogger(AlbumController.class);

    @Autowired
    private MPDBean mpd;

    @RequestMapping(path = "/api/album", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Album>> listAlbums(@RequestParam(name = "artist", required = false) String artist) {
        AlbumDatabase albumDatabase = mpd.getMpd().getMusicDatabase().getAlbumDatabase();
        Collection<MPDAlbum> albums = Strings.isNullOrEmpty(artist)
                ? albumDatabase.listAllAlbums()
                : albumDatabase.listAlbumsByArtist(new MPDArtist(artist));

        if (albums == null || albums.isEmpty()) {
            LOG.info("No albums found for artist: {}", artist);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        LOG.info("Found {} albums for artist: {}", albums.size(), artist);
        return ResponseEntity.ok(Converter.convertAlbums(albums));
    }

    @RequestMapping(path = "/api/album/{album}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Song>> listSongs(@PathVariable("album") String album) {
        SongDatabase songDatabase = mpd.getMpd().getMusicDatabase().getSongDatabase();
        Collection<MPDSong> songs = songDatabase.findAlbum(album);

        if (songs == null || songs.isEmpty()) {
            LOG.info("No songs found for album: {}", album);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        LOG.info("Found {} songs for album: {}", songs.size(), album);
        return ResponseEntity.ok(Converter.convertSongs(songs));
    }
}
