package com.concertrip.server.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created hyunjk on 2018-12-29.
 * Github : https://github.com/hyunjkluz
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="genres")
public class Genres {
    @Id
    private String _id;
    private String profileImg;
    private String backImg;
    private String name;
    private String youtubeUrl;
}
