package com.kakaopay.api.domain;

import com.kakaopay.api.domain.commons.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Support extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name ="institution_id", foreignKey = @ForeignKey(name = "FK_SUPPORT_INSTITUTION"))
    private Institution institution;

    private String supportTarget;

    @Enumerated(value = EnumType.STRING)
    private UseType useType;

    private Long limitAmount;

    @Embedded
    private Rate rate;

    private String suggestedInstitution;

    private String management;

    private String reception;
}
