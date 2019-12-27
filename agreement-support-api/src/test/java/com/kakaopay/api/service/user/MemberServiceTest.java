package com.kakaopay.api.service.user;

import com.kakaopay.api.domain.member.Member;
import com.kakaopay.api.domain.member.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void after(){
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원 가입에 성공한다.")
    void joinMember() {
        //given

        //when
        Member member = memberService.joinMember("memberId", "password");

        //then
        assertAll("member value check..", ()->{
            assertEquals("memberId", member.getMemberId());
            assertEquals("5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8", member.getPassword());
        });
    }

    @Test
    @DisplayName("로그인 실패 시 GUEST라는 id를 리턴한다.")
    void loginFail() {
        //given
        memberService.joinMember("memberId", "password");

        //when
        Member login = memberService.login("test", "test");

        //then
        assertThat(login.getMemberId(), is("GUEST"));
    }

    @Test
    @DisplayName("로그인 성공한다.")
    void loginSuccess() {
        //given
        memberService.joinMember("memberId", "password");

        //when
        Member login = memberService.login("memberId", "password");

        //then
        assertThat(login.getMemberId(), is("memberId"));
    }
}