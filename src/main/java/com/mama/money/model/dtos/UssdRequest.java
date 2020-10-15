package com.mama.money.model.dtos;

import lombok.Getter;

@Getter
public class UssdRequest {

    private String sessionId;

    private String msisdn;

    private String userEntry = "";
}
