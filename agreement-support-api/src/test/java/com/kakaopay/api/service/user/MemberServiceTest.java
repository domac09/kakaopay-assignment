package com.kakaopay.api.service.user;

import com.kakaopay.api.domain.member.Member;
import com.kakaopay.api.domain.member.MemberRepository;
import org.junit.jupiter.api.AfterEach;
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
    void joinMember() {
        Member member = memberService.joinMember("memberId", "password");

        assertAll("member value check..", ()->{
            assertEquals("memberId", member.getMemberId());
            assertEquals("5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8", member.getPassword());
        });
    }

    @Test
    void loginFail() {
        memberService.joinMember("memberId", "password");

        Member login = memberService.login("test", "test");

        assertThat(login.getMemberId(), is("GUEST"));
    }

    @Test
    void loginSuccess() {
        memberService.joinMember("memberId", "password");

        Member login = memberService.login("memberId", "password");

        assertThat(login.getMemberId(), is("memberId"));
    }
}