package com.github.iweinzierl.mpdrestproxy.controller;

import com.github.iweinzierl.mpdrestproxy.domain.Album;
import com.github.iweinzierl.mpdrestproxy.domain.Song;
import com.github.iweinzierl.mpdrestproxy.helper.Converter;
import com.github.iweinzierl.mpdrestproxy.helper.MPDBean;
import org.bff.javampd.album.AlbumDatabase;
import org.bff.javampd.album.MPDAlbum;
import org.bff.javampd.song.MPDSong;
import org.bff.javampd.song.SongDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
public class SongController {

    private static final Logger LOG = LoggerFactory.getLogger(SongController.class);

    @Autowired
    private MPDBean mpdBean;

    @RequestMapping(path = "/api/song", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Song>> listSongs() {
        AlbumDatabase albumDatabase = mpdBean.getMpd().getMusicDatabase().getAlbumDatabase();
        SongDatabase songDatabase = mpdBean.getMpd().getMusicDatabase().getSongDatabase();

        Collection<MPDAlbum> mpdAlbums = albumDatabase.listAllAlbums();
        Collection<Album> albums = Converter.convertAlbums(mpdAlbums);

        return ResponseEntity.ok(getAllSongs(songDatabase, albums));
    }

    private Collection<Song> getAllSongs(SongDatabase songDatabase, Collection<Album> albums) {
        List<Song> allSongs = new ArrayList<>();

        for (Album album : albums) {
            LOG.debug("Find songs of album: {} ({})", album.getName(), album.getArtist().getName());
            Collection<MPDSong> mpdSongs = songDatabase.findAlbum(album.getName());

            if (mpdSongs != null && !mpdSongs.isEmpty()) {
                allSongs.addAll(Converter.convertSongs(mpdSongs));
            }
        }

        return allSongs;
    }
}
