package com.github.iweinzierl.mpdrestproxy.controller;

import com.github.iweinzierl.mpdrestproxy.domain.PlayerInfo;
import com.github.iweinzierl.mpdrestproxy.domain.PlayerStatus;
import com.github.iweinzierl.mpdrestproxy.helper.Converter;
import com.github.iweinzierl.mpdrestproxy.helper.MPDBean;
import org.bff.javampd.MPDException;
import org.bff.javampd.player.Player;
import org.bff.javampd.playlist.Playlist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlayerController {

    @Autowired
    private MPDBean mpdBean;

    @RequestMapping(path = "/api/player", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PlayerInfo> getPlayerInfo() {
        Playlist playlist = mpdBean.getMpd().getPlaylist();
        Player player = mpdBean.getMpd().getPlayer();
        Player.Status status = player.getStatus();

        PlayerInfo playerInfo = new PlayerInfo();
        playerInfo.setVolume(player.getVolume());
        playerInfo.setCurrentSong(Converter.convertSong(player.getCurrentSong()));
        playerInfo.setTotalTime(player.getTotalTime());
        playerInfo.setElapsedTime(player.getElapsedTime());
        playerInfo.setPlaylist(Converter.convertSongs(playlist.getSongList()));

        switch (status) {
            case STATUS_STOPPED:
                playerInfo.setStatus(PlayerStatus.STOPPED);
                break;
            case STATUS_PAUSED:
                playerInfo.setStatus(PlayerStatus.PAUSED);
                break;
            case STATUS_PLAYING:
                playerInfo.setStatus(PlayerStatus.PLAYING);
                break;
        }

        return ResponseEntity.ok(playerInfo);
    }

    @RequestMapping(path = "/api/player/play", method = RequestMethod.GET)
    public ResponseEntity play() {
        Player player = mpdBean.getMpd().getPlayer();

        try {
            player.play();

            Player.Status status = player.getStatus();
            if (status == Player.Status.STATUS_PLAYING) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        } catch (MPDException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(path = "/api/player/pause", method = RequestMethod.GET)
    public ResponseEntity pause() {
        Player player = mpdBean.getMpd().getPlayer();

        try {
            player.pause();

            Player.Status status = player.getStatus();
            if (status == Player.Status.STATUS_PAUSED) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        } catch (MPDException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(path = "/api/player/next", method = RequestMethod.GET)
    public ResponseEntity playNext() {
        Player player = mpdBean.getMpd().getPlayer();

        try {
            player.playNext();
            return ResponseEntity.ok().build();
        } catch (MPDException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(path = "/api/player/previous", method = RequestMethod.GET)
    public ResponseEntity playPrevious() {
        Player player = mpdBean.getMpd().getPlayer();

        try {
            player.playPrevious();
            return ResponseEntity.ok().build();
        } catch (MPDException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(path = "/api/player/stop", method = RequestMethod.GET)
    public ResponseEntity stop() {
        Player player = mpdBean.getMpd().getPlayer();

        try {
            player.stop();

            Player.Status status = player.getStatus();
            if (status == Player.Status.STATUS_STOPPED) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        } catch (MPDException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(path = "/api/player/volume", method = RequestMethod.GET)
    public ResponseEntity setVolume(@RequestParam("volume") int volume) {
        Player player = mpdBean.getMpd().getPlayer();

        try {
            player.setVolume(volume);
            return ResponseEntity.ok().build();
        } catch (MPDException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}
