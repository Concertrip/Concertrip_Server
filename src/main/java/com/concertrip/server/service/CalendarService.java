package com.concertrip.server.service;

import com.concertrip.server.dal.ArtistsDAL;
import com.concertrip.server.dal.EventsDAL;
import com.concertrip.server.model.DefaultRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Created hyunjk on 2018-12-27.
 * Github : https://github.com/hyunjkluz
 */
@Slf4j
@Service("calendar")
public class CalendarService {
    private final ArtistsDAL artistsDAL;
    private final EventsDAL eventsDAL;

    public CalendarService(ArtistsDAL artistsDAL, EventsDAL eventsDAL) {
        this.artistsDAL = artistsDAL;
        this.eventsDAL = eventsDAL;
    }




}
