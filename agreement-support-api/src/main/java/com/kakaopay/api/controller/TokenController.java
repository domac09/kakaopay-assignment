package com.kakaopay.api.controller;

import com.kakaopay.api.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class TokenController {

    private final AuthService authService;

    @PostMapping("/v1/token/refresh")
    public TokenResponse refreshToken(@RequestHeader(value = "Authorization") String value) {
        String[] values = Optional.ofNullable(StringUtils.split(value, "Bearer")).orElse(new String[0]);
        if (StringUtils.hasText(values[1])) {
            return new TokenResponse(authService.refreshToken(values[1]));
        }
        return new TokenResponse();
    }
}
