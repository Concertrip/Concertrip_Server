package com.concertrip.server.dto;

import lombok.Data;

import java.util.Date;

/**
 * Created by HYEON on 2019-01-09.
 */

@Data
public class Notice {
    private int id;
    private int userIdx;
    private String title;
    private String body;
    private Date createdAt;
    private String noticeImg;
}
