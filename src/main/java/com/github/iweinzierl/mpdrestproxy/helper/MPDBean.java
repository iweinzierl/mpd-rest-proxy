package com.github.iweinzierl.mpdrestproxy.helper;

import org.bff.javampd.server.MPD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.UnknownHostException;

@Component
public class MPDBean {

    @Value("${mpd.host}")
    private String mpdHost;

    @Value("${mpd.port}")
    private int mpdPort;

    @Value("${mpd.connection.timeout}")
    private int mpdConnectionTimeout;

    private static final Logger LOG = LoggerFactory.getLogger(MPDBean.class);

    @Autowired
    private MPDPlayerLogger playerLogger;

    private MPD mpd;

    @PostConstruct
    public void init() throws UnknownHostException {
        mpd = new MPD.Builder()
                .server(mpdHost)
                .port(mpdPort)
                .timeout(mpdConnectionTimeout)
                .build();

        mpd.getPlayer().addPlayerChangeListener(playerLogger);
        mpd.getPlayer().addVolumeChangeListener(playerLogger);

        LOG.info("Finished setup of MPD");
    }

    @PreDestroy
    public void destroy() {
        mpd.close();
    }

    public MPD getMpd() {
        return mpd;
    }
}
