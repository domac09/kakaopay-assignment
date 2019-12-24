package com.kakaopay.api.domain;

import com.kakaopay.api.domain.commons.auditing.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Support extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String institutionCode;

    private String supportTarget;

    private String useType;

    private double maximum;

    private double minimum;

    private String secondaryConservation;

    private String suggestedInstitution;

    private String management;

    private String distributor;
}
