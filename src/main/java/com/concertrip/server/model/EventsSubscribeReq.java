package com.concertrip.server.model;

import lombok.Data;

/**
 * Created hyunjk on 2018-12-28.
 * Github : https://github.com/hyunjkluz
 */
@Data
public class EventsSubscribeReq {
    private int userIdx;
    private String eventId;
}
