package com.kakaopay.api.controller;

import com.kakaopay.api.domain.member.Member;
import com.kakaopay.api.service.auth.AuthService;
import com.kakaopay.api.service.user.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final AuthService authService;

    @PostMapping
    public TokenResponse joinMember(@RequestBody @Valid MemberDto memberDto) {
        Member member = memberService.joinMember(memberDto.getId(), memberDto.getPassword());
        return new TokenResponse(authService.createToken(member.getMemberId()));
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody @Valid MemberDto memberDto) {
        Member member = memberService.login(memberDto.getId(), memberDto.getPassword());

        if (member.getMemberId().equals("GUEST")) {
            return new TokenResponse("", "this id is not valid.");
        }

        return new TokenResponse(authService.createToken(member.getMemberId()));
    }


}
