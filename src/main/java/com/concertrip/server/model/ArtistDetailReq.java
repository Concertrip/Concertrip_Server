package com.concertrip.server.model;

import com.concertrip.server.domain.Events;
import com.concertrip.server.dto.Precaution;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * Created by HYEON on 2018-12-29.
 */

@Data
@Document(collection="artists")
public class ArtistDetailReq {
    @Id
    private String _id;
    private boolean isSubscribe;
    private String profileImg;
    private String backImg;
    private String name;
    private int subscribeNum;
    private String youtubeUrl;
    private List<CommonListReq> memberList;
    private List<CommonListReq> eventsList;
}