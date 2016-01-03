package com.github.iweinzierl.mpdrestproxy.controller;

import com.github.iweinzierl.mpdrestproxy.domain.Artist;
import com.github.iweinzierl.mpdrestproxy.helper.MPDBean;
import org.bff.javampd.artist.ArtistDatabase;
import org.bff.javampd.artist.MPDArtist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
public class ArtistController {

    private static final Logger LOG = LoggerFactory.getLogger(ArtistController.class);

    @Autowired
    private MPDBean mpd;

    @RequestMapping(path = "/api/artist", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Artist>> listArtists() {
        ArtistDatabase artistDatabase = mpd.getMpd().getMusicDatabase().getArtistDatabase();
        Collection<MPDArtist> mpdArtists = artistDatabase.listAllArtists();

        if (mpdArtists == null || mpdArtists.isEmpty()) {
            LOG.info("No artists found!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(mpdArtists.parallelStream().map(
                mpdArtist -> new Artist(mpdArtist.getName())
        ).collect(Collectors.toList()));
    }
}
