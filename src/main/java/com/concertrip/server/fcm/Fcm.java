package com.concertrip.server.fcm;

import lombok.Data;

import java.util.Map;

/**
 * Created by HYEON on 2019-01-05.
 */

@Data
public class Fcm {
    private String to;
    private Map<String, String> notification;
}
