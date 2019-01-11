package com.concertrip.server.model;

import lombok.Data;

/**
 * Created by HYEON on 2019-01-09.
 */

@Data
public class FcmReq {
    private String type;
    private String objIdx;
    private String title;
    private String body;
}
