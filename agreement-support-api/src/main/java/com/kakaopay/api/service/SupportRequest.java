package com.kakaopay.api.service;

import com.kakaopay.api.domain.Institution;
import com.kakaopay.api.domain.Rate;
import com.kakaopay.api.domain.Support;
import com.kakaopay.api.domain.UseType;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;

/**
 * {
 *     "region": "강릉시",
 *     "target": "강릉시 소재 중소기업으로서 강릉시장이 추천한 자", "usage": "운전",
 *     "limit" : "추천금액 이내",
 *     "rate": "3.00%",
 *     "institute": "강릉시",
 *     "mgmt": "강릉지점",
 *     "reception": "강릉시 소재 영업점"
 * }
 */

@Getter
public class SupportRequest {
    private String region;
    private String target;
    private String usage;
    @NotEmpty
    private long limit;
    @NotEmpty
    private double maximumRate;
    @NotEmpty
    private double minimumRate;
    private String institute;
    private String mgmt;
    private String reception;

    @Builder
    public SupportRequest(String region, String target, String usage, long limit, double maximumRate, double minimumRate, String institute, String mgmt, String reception) {
        this.region = region;
        this.target = target;
        this.usage = usage;
        this.limit = limit;
        this.maximumRate = maximumRate;
        this.minimumRate = minimumRate;
        this.institute = institute;
        this.mgmt = mgmt;
        this.reception = reception;
    }

    public static Support toEntity(SupportRequest supportRequest, Institution institution){
        return Support.builder()
                .institution(institution)
                .limitAmount(supportRequest.getLimit())
                .rate(Rate.builder().maximum(supportRequest.getMaximumRate()).minimum(supportRequest.getMinimumRate()).build())
                .management(supportRequest.getMgmt())
                .reception(supportRequest.getReception())
                .suggestedInstitution(supportRequest.getInstitute())
                .supportTarget(supportRequest.getTarget())
                .useType(UseType.of(supportRequest.getUsage()))
                .build();
    }
}
