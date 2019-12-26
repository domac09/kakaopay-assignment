package com.kakaopay.api.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenResponse {
    private String token;
    private String message;

    public TokenResponse(String token) {
        this.token = token;
        this.message = "OK";
    }

    public TokenResponse(String token, String message) {
        this.token = token;
        this.message = message;
    }


}
