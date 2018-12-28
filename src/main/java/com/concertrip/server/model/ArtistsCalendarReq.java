package com.concertrip.server.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.HashMap;
import java.util.List;

/**
 * Created hyunjk on 2018-12-28.
 * Github : https://github.com/hyunjkluz
 */
@Data
public class ArtistsCalendarReq {
    @Id
    private String _id;
    private String name;
    private HashMap<String, List<EventsReq>> eventsList;

    public ArtistsCalendarReq(String _id, String name) {
        this._id = _id;
        this.name = name;
    }
}
