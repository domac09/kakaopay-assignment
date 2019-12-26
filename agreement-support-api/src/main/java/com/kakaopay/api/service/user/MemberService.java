package com.kakaopay.api.service.user;

import com.kakaopay.api.domain.member.Member;
import com.kakaopay.api.domain.member.MemberRepository;
import com.kakaopay.api.utils.SHA256Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member joinMember(String memberId, String password) {
        Member member = Member.builder()
                .memberId(memberId)
                .password(SHA256Utils.encrypt(password))
                .build();

        return memberRepository.save(member);
    }

    public Member login(String memberId, String password) {
        return memberRepository.findMemberByMemberIdAndPassword(memberId, SHA256Utils.encrypt(password))
                .orElse(Member.builder().memberId("GUEST").build());
    }
}
