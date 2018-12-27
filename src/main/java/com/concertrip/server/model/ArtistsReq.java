package com.concertrip.server.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created hyunjk on 2018-12-27.
 * Github : https://github.com/hyunjkluz
 */
@Data
@Document(collection="artists")
public class ArtistsReq {
    @Id
    private String _id;
    private String name;
    private List<String> tag;

}
