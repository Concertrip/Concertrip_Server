package com.concertrip.server.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * Created hyunjk on 2018-12-25.
 * Github : https://github.com/hyunjkluz
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="events")
public class Events implements Serializable {
    @Id
    private String _id;
    @NonNull
    private String name;
    @NonNull
    private String profileImg;
    private String backImg;
    @NonNull
    private String location;
    private String region;
    private String[] member;
    private Date[] date;
    private String[] seatName;
    private String[] seatPrice;
    private String eventInfoImg;
    private String ticketImg;
    private int[] precaution;
    private String[] filter;
    private Integer[] subscriber;
}
