package com.kakaopay.api.service;

import com.kakaopay.api.domain.Support;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.text.DecimalFormat;

@ToString
@Getter
public class SupportResponse {

    private String region;
    private String target;
    private String limit;
    private String rate;
    private String usage;
    private String institute;
    private String mgmt;
    private String reception;

    @Builder
    public SupportResponse(String region, String target, String limit, String rate, String usage, String institute, String mgmt, String reception) {
        this.region = region;
        this.target = target;
        this.limit = limit;
        this.rate = rate;
        this.usage = usage;
        this.institute = institute;
        this.mgmt = mgmt;
        this.reception = reception;
    }

    public static SupportResponse toEntity(Support support) {
        DecimalFormat df = new DecimalFormat("#,###");
        return SupportResponse.builder()
                .institute(support.getSuggestedInstitution())
                .limit(support.getLimitAmount() == 0 ? "추천금액 이내" : df.format(support.getLimitAmount()) + " 이내")
                .mgmt(support.getManagement())
                .rate(support.getRate().getMinimum() == support.getRate().getMaximum() ? support.getRate().getMaximum() + "%" : support.getRate().getMinimum() + "%~" + support.getRate().getMaximum() + "%")
                .reception(support.getReception())
                .region(support.getInstitution().getName())
                .target(support.getSupportTarget())
                .usage(support.getUseType().getType())
                .build();
    }
}
