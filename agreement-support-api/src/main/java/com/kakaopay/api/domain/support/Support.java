package com.kakaopay.api.domain.support;

import com.kakaopay.api.domain.commons.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@ToString
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Support extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name ="region_id", foreignKey = @ForeignKey(name = "FK_SUPPORT_INSTITUTION"))
    private Region region;

    private String supportTarget;

    @Enumerated(value = EnumType.STRING)
    private UseType useType;

    private long limitAmount;

    @Embedded
    private Rate rate;

    private String suggestedInstitution;

    private String management;

    private String reception;

    public void update(Support support){
        this.region = support.region;
        this.supportTarget = support.supportTarget;
        this.useType = support.useType;
        this.limitAmount = support.limitAmount;
        this.rate = support.rate;
        this.suggestedInstitution = support.suggestedInstitution;
        this.management = support.management;
        this.reception = support.reception;
    }
}
