package com.concertrip.server.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * Created by HYEON on 2018-12-26.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="artists")
public class Artists implements Serializable {
    @Id
    private String _id;
    @NonNull
    private String profileImg;
    private String backImg;
    @NonNull
    private String name;
    private String youtubeUrl;
    private String[] filter;
    private String[] member;
    private Integer[] subscriber;

}
