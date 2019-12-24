package com.kakaopay.api.domain;

import lombok.*;

import javax.persistence.Embeddable;

@ToString
@Getter
@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Rate {
    private double minimum;

    private double maximum;
}
