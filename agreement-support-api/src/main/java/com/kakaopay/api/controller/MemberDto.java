package com.kakaopay.api.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    @NotBlank
    private String id;
    @NotBlank
    private String password;
}
