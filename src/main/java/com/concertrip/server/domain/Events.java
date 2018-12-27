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
    private String title;
    @NonNull
    private String profileImg;
    private String backImg;
    @NonNull
    private String location;
    private String[] tag;
    @NonNull
    private String[] cast;
    @NonNull
    private Date[] date;
    private int price;
    private String youtubeUrl;

}
