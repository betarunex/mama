package com.mama.money.model.dtos;

import lombok.Getter;

@Getter
public class UssdResponse {
    private String sessionId;

    private String message;

    public UssdResponse(final String sessionId, final String message) {
        this.sessionId = sessionId;
        this.message = message;
    }
}
