package com.kakaopay.api.config.filter;

import com.kakaopay.api.utils.JwtTokenUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
class AuthenticationFilterTest {
    private static final String TEST_URI = "/v1/supports";
    private static final String EXCEPT_URI = "/v1/members";
    private static final String SECRET_KEY = "secretKey";

    private MockHttpServletRequest httpServletRequest;
    private MockHttpServletResponse httpServletResponse;
    private MockFilterChain filterChain;

    private AuthenticationFilter authenticationFilter;

    @BeforeEach
    void before() {
        httpServletRequest = new MockHttpServletRequest();
        httpServletResponse = new MockHttpServletResponse();
        filterChain = new MockFilterChain();
        authenticationFilter = new AuthenticationFilter(SECRET_KEY);
    }

    @Test
    @DisplayName("jwt token 인증을 정상 통과한다.")
    void filterOk() throws ServletException, IOException {
        //given
        String token = createToken("memberId");

        httpServletRequest.addHeader("Authorization", "Bearer " + token);
        httpServletRequest.setRequestURI(TEST_URI);

        //when
        authenticationFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);

        //then
        assertThat(httpServletResponse.getStatus(), is(200));
    }

    @Test
    @DisplayName("EXCEPT_URIS에 포함 된 경우 인증을 통과한다.")
    void authenticationExceptUri() throws ServletException, IOException {
        //given
        httpServletRequest.setRequestURI(EXCEPT_URI);

        //when
        authenticationFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);

        //then
        assertThat(httpServletResponse.getStatus(), is(200));
    }

    @Test
    @DisplayName("[unauthorized] token의 memberId가 GUEST 일 때 인증 실패된다.")
    void invalidAuthKeyByMemberId() throws ServletException, IOException {
        //given
        String token = createToken("GUEST");

        httpServletRequest.addHeader("Authorization", "Bearer " + token);
        httpServletRequest.setRequestURI(TEST_URI);

        //when
        authenticationFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);

        //then
        assertThat(httpServletResponse.getStatus(), is(401));
        assertThat(httpServletResponse.getContentAsString(), is("{\"error\": \"unauthorized\"}"));
    }

    @Test
    @DisplayName("[unauthorized] header에 Authorization 항목이 없을 때 인증 실패된다.")
    void invalidAuthKeyHeaderEmpty() throws ServletException, IOException {
        //given
        httpServletRequest.setRequestURI(TEST_URI);

        //when
        authenticationFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);

        //then
        assertThat(httpServletResponse.getStatus(), is(401));
        assertThat(httpServletResponse.getContentAsString(), is("{\"error\": \"unauthorized\"}"));
    }

    @Test
    @DisplayName("[unauthorized] token format이 이상한 경우 (Bearer가 빠진 경우) 인증이 실패된다.")
    void invalidAuthKeyTokenFormat() throws ServletException, IOException {
        //given
        String token = createToken("memberId");

        httpServletRequest.addHeader("Authorization", token);
        httpServletRequest.setRequestURI(TEST_URI);

        //when
        authenticationFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);

        //then
        assertThat(httpServletResponse.getStatus(), is(401));
        assertThat(httpServletResponse.getContentAsString(), is("{\"error\": \"unauthorized\"}"));
    }

    @Test
    @DisplayName("[unauthorized] token이 verify 되지 않으면 인증이 실패된다.")
    void invalidAuthKeyTokenAbnormal() throws ServletException, IOException {
        //given
        String token = "TEST";

        httpServletRequest.addHeader("Authorization", "Bearer " + token);
        httpServletRequest.setRequestURI(TEST_URI);

        //when
        authenticationFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);

        //then
        assertThat(httpServletResponse.getStatus(), is(401));
        assertThat(httpServletResponse.getContentAsString(), is("{\"error\": \"unauthorized\"}"));
    }

    @Test
    @DisplayName("[unauthorized] 명시되지 않은 url은 인증이 실패된다.")
    void unknownUri() throws ServletException, IOException {
        //given
        httpServletRequest.setRequestURI("/v1/test");

        //when
        authenticationFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);

        //then
        assertThat(httpServletResponse.getStatus(), is(401));
        assertThat(httpServletResponse.getContentAsString(), is("{\"error\": \"unauthorized\"}"));
    }

    @Test
    @DisplayName("[expired] token이 만료된 경우 인증이 실패된다.")
    void tokenExpired() throws ServletException, IOException {
        //given
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJrYWthb3BheSIsImV4cCI6MTU3NzM0OTc4MywiaWF0IjoxNTc3MzQ5NzIzLCJtZW1iZXJJZCI6InRlc3QifQ.miNuU5KXyGM_k3GVHWjHbkGNPvTI-zrR8ZhXbIYRhkk";

        httpServletRequest.addHeader("Authorization", "Bearer " + token);
        httpServletRequest.setRequestURI(TEST_URI);

        //when
        authenticationFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);

        //then
        assertThat(httpServletResponse.getStatus(), is(401));
        assertThat(httpServletResponse.getContentAsString(), is("{\"error\": \"token_expired\"}"));
    }

    private String createToken(String memberId) {
        return Optional.ofNullable(JwtTokenUtils.createToken(SECRET_KEY, memberId)).orElse("");
    }
}