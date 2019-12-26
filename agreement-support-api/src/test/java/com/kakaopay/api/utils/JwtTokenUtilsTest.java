package com.kakaopay.api.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


class JwtTokenUtilsTest {

    @Test
    @DisplayName("토큰을 생성해본다.")
    void createToken() {
        String token = JwtTokenUtils.createToken("secretKey", "memberId");
        System.out.println("token = " + token);
    }

    @Test
    @DisplayName("토큰을 생성 후 verify 통과한다.")
    void verifyToken() {
        String token = JwtTokenUtils.createToken("secretKey", "memberId");
        boolean verifyToken = JwtTokenUtils.verifyToken("secretKey", token);

        assertThat(verifyToken, is(true));
    }

    @Test
    @DisplayName("expired 토큰 verify 실패한다.")
    void verifyTokenFail() {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJrYWthb3BheSIsImV4cCI6MTU3NzI2NzU1OCwiaWF0IjoxNTc3MjY1NzU4LCJtZW1iZXJJZCI6Im1lbWJlcklkIn0.3N0jp4-T7smBu5JWGWVKr1EcSkyFmtQRf_TT5bGYF1s";
        boolean verifyToken = JwtTokenUtils.verifyToken("secretKey", token);

        assertThat(verifyToken, is(false));
    }

    @Test
    @DisplayName("토큰을 decode 한다.")
    void decode() {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJrYWthb3BheSIsImV4cCI6MTU3NzI2NzU1OCwiaWF0IjoxNTc3MjY1NzU4LCJtZW1iZXJJZCI6Im1lbWJlcklkIn0.3N0jp4-T7smBu5JWGWVKr1EcSkyFmtQRf_TT5bGYF1s";
        DecodedToken decodedToken = JwtTokenUtils.decodeToken(token);

        assertThat(decodedToken.getIssuer(), is("kakaopay"));
        assertThat(decodedToken.getMemberId(), is("memberId"));
    }
}