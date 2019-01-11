package com.concertrip.server.model;

import com.concertrip.server.dto.Precaution;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * Created hyunjk on 2018-12-28.
 * Github : https://github.com/hyunjkluz
 */
@Data
@Document(collection="events")
public class EventsDetailReq {
    @Id
    private String _id;
    private boolean subscribe = false;
    private String profileImg;
    private String backImg;
    private String name;
    private int subscribeNum;
    private String youtubeUrl;
    private String location;
    private List<CommonListReq> memberList;
    private Date[] date;
    private String[] seatName;
    private String[] seatPrice;
    private List<Precaution> precautionList;
    private String eventInfoImg;
    private Boolean purchase = false;
}
