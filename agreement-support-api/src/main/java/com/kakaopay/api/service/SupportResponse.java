package com.kakaopay.api.service;

import com.kakaopay.api.domain.Institution;
import com.kakaopay.api.domain.Support;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.text.DecimalFormat;

@ToString
@Getter
public class SupportResponse {
    /**
     * {
     * "region":"강릉시",
     * "target":"강릉시 소재 중소기업으로서 강릉시장이 추천한 자", "usage":"운전",
     * "limit" :"추천금액 이내",
     * "rate":"3.00%",
     * "institute":"강릉시",
     * "mgmt":"강릉지점",
     * "reception":"강릉시 소재 영업점"
     * }
     */

    private String region;
    private String target;
    private String limit;
    private String rate;
    private String institute;
    private String mgmt;
    private String reception;

    @Builder
    public SupportResponse(String region, String target, String limit, String rate, String institute, String mgmt, String reception) {
        this.region = region;
        this.target = target;
        this.limit = limit;
        this.rate = rate;
        this.institute = institute;
        this.mgmt = mgmt;
        this.reception = reception;
    }

    public static SupportResponse toEntity(Support support, Institution institution) {
        DecimalFormat df = new DecimalFormat("#,###");
        return SupportResponse.builder()
                .institute(support.getSuggestedInstitution())
                .limit(support.getLimitAmount() == 0 ? "추천금액 이내" : df.format(support.getLimitAmount()) + " 이내")
                .mgmt(support.getManagement())
                .rate(support.getRate().getMinimum() == support.getRate().getMaximum() ? support.getRate().getMaximum() + "%" : support.getRate().getMinimum() + "%~" + support.getRate().getMaximum() + "%")
                .reception(support.getReception())
                .region(institution.getName())
                .target(support.getSupportTarget())
                .build();
    }
}
