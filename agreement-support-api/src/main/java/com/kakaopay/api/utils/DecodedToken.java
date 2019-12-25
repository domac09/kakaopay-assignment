package com.kakaopay.api.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
@AllArgsConstructor
public class DecodedToken {
    private String issuer;
    private String memberId;
}
