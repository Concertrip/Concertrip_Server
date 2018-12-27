package com.concertrip.server.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created hyunjk on 2018-12-26.
 * Github : https://github.com/hyunjkluz
 */
@Data
@Document(collection="events")
public class EventsReq {
    @Id
    private String _id;
    private String title;

}
