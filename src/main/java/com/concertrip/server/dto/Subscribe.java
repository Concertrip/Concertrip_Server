package com.concertrip.server.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * Created hyunjk on 2018-12-29.
 * Github : https://github.com/hyunjkluz
 */
@Data
public class Subscribe {
    @Id
    private String id;
    private int userIdx;
    private String type;
    private String objIdx;
}
