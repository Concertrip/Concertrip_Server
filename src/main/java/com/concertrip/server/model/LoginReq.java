package com.concertrip.server.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginReq {
    private String id;
    private String password;
}