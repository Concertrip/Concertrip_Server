package com.concertrip.server.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
public class Artists {
    @Id
    private String _id;
    private String profileImg;
    private String backImg;
    private String name;
    private String youtubeUrl;
    private String[] tag;
}
