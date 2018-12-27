package com.concertrip.server.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

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
    private String profileImg;
    private List<Date> date;

}
