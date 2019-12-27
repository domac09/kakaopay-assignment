package com.kakaopay.api.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@Slf4j
class JwtTokenUtilsTest {
    private static final String SECRET_KEY = "secretKey";

    @Test
    @DisplayName("토큰을 생성해본다.")
    void createToken() {
        //given
        //when
        String token = JwtTokenUtils.createToken(SECRET_KEY, "memberId");
        //then
        log.info("token={}", token);
        assertThat(token, notNullValue());
        assertThat(token.split("\\.").length, is(3));
    }

    @Test
    @DisplayName("토큰을 생성 후 verify 통과한다.")
    void verifyToken() {
        //given
        String token = JwtTokenUtils.createToken(SECRET_KEY, "memberId");

        //when
        boolean verifyToken = JwtTokenUtils.verifyToken(SECRET_KEY, token);

        //then
        assertThat(verifyToken, is(true));
    }

    @Test
    @DisplayName("expired 토큰 verify 실패한다.")
    void verifyTokenFail() {
        //given
        String expiredToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJrYWthb3BheSIsImV4cCI6MTU3NzI2NzU1OCwiaWF0IjoxNTc3MjY1NzU4LCJtZW1iZXJJZCI6Im1lbWJlcklkIn0.3N0jp4-T7smBu5JWGWVKr1EcSkyFmtQRf_TT5bGYF1s";

        //when
        boolean verifyToken = JwtTokenUtils.verifyToken(SECRET_KEY, expiredToken);

        //then
        assertThat(verifyToken, is(false));
    }

    @Test
    @DisplayName("토큰을 decode 한다.")
    void decode() {
        //given
        String token = JwtTokenUtils.createToken(SECRET_KEY, "memberId");

        //when
        DecodedToken decodedToken = JwtTokenUtils.decodeToken(token);

        //then
        assertThat(decodedToken.getIssuer(), is("kakaopay"));
        assertThat(decodedToken.getMemberId(), is("memberId"));
    }
}