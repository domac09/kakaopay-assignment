package com.kakaopay.api.service.support;

import com.kakaopay.api.domain.support.Rate;
import com.kakaopay.api.domain.support.Region;
import com.kakaopay.api.domain.support.Support;
import com.kakaopay.api.domain.support.UseType;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;

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

    public static Support toEntity(SupportRequest supportRequest, Region region){
        return Support.builder()
                .region(region)
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
