package com.kakaopay.api.domain;

import com.kakaopay.api.domain.commons.BaseEntity;
import lombok.*;

import javax.persistence.*;

@ToString
@Getter
@Entity
@Builder
@AllArgsConstructor
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

    private long limitAmount;

    @Embedded
    private Rate rate;

    private String suggestedInstitution;

    private String management;

    private String reception;
}
