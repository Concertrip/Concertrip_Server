package com.concertrip.server.model;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

/**
 * Created hyunjk on 2018-12-28.
 * Github : https://github.com/hyunjkluz
 */
@Data
public class CalendarReq {
    private HashMap<String, List<EventsReq>> events;
    private List<ArtistsCalendarReq> artists;
}
