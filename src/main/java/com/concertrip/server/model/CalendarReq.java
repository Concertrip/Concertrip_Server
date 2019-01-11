package com.concertrip.server.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * Created hyunjk on 2019-01-02.
 * Github : https://github.com/hyunjkluz
 */
@Data
public class CalendarReq {
    @Id
    private String _id;
    private String tabId;
    private String name;
    private String profileImg;
    private Date[] date;
    private String[] tag;
    private boolean subscribe = false;
    private String hashTag;
}
