package com.kakaopay.api.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Getter
@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Rate {
    private double minimum;

    private double maximum;
}
