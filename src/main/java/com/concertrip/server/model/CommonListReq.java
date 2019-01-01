package com.concertrip.server.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * Created hyunjk on 2018-12-29.
 * Github : https://github.com/hyunjkluz
 */
@Data
public class CommonListReq {
    @Id
    private String _id;
    private String name;
    private String profileImg;
    private boolean subscribe;
}
