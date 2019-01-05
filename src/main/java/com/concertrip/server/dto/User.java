package com.concertrip.server.dto;

import lombok.Data;

@Data
public class User {
    private int userIdx;
    private String id;
    private String password;
    private String name;
    private String fcmToken;
}
