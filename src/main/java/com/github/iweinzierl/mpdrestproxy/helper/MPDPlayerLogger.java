package com.github.iweinzierl.mpdrestproxy.helper;

import org.bff.javampd.player.PlayerChangeEvent;
import org.bff.javampd.player.PlayerChangeListener;
import org.bff.javampd.player.VolumeChangeEvent;
import org.bff.javampd.player.VolumeChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MPDPlayerLogger implements PlayerChangeListener, VolumeChangeListener {

    private static final Logger LOG = LoggerFactory.getLogger(MPDPlayerLogger.class);

    @Override
    public void playerChanged(PlayerChangeEvent event) {
        LOG.debug("-> player changed event: {} ({})", event.getEvent(), event.getSource());
    }

    @Override
    public void volumeChanged(VolumeChangeEvent event) {
        LOG.debug("-> player volume changed event: {} ({})", event.getVolume(), event.getSource());
    }
}
