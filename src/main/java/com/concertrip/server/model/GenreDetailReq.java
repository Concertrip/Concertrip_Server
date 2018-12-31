package com.concertrip.server.model;

import lombok.Data;

import java.util.List;

@Data
public class GenreDetailReq {
    private String _id;
    private String profileImg;
    private String backImg;
    private String name;
    private Boolean isSubscribe;
    private Integer subscribeNum;
    private String youtubeUrl;
    private List<CommonListReq> eventList;
}
