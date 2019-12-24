package com.kakaopay.api.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum UseType {
    DRIVE("운전"), FACILITY("시설"), BOTH("운전 및 시설");

    private String type;

    public static UseType of(String type) {
        return Arrays.stream(UseType.values())
                .filter(data -> type.equals(data.getType()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당되는 타입이 없습니다."));

    }
}
