package com.kakaopay.api.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Getter
@Embeddable
@NoArgsConstructor
public class Rate {
    private double minimum;

    private double maximum;
}
