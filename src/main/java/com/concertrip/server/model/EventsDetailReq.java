package com.concertrip.server.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created hyunjk on 2018-12-28.
 * Github : https://github.com/hyunjkluz
 */
@Data
@Document(collection="events")
public class EventsDetailReq {
    @Id
    private String _id;
    private String title;
    private String profileImg;
    private String backImg;
    private String location;
    private String[] tag;
    private String[] cast;
    private String[] castImg;
    private Date[] date;
    private String[] seats;
    private int[] price;
    private String youtubeUrl;
    private String eventInfoImg;

}
