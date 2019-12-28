package com.kakaopay.api.service.support;

import com.kakaopay.api.domain.region.Region;
import com.kakaopay.api.domain.region.RegionRepository;
import com.kakaopay.api.domain.support.Support;
import com.kakaopay.api.domain.support.SupportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AgreementSupportService {

    private final SupportRepository supportRepository;
    private final RegionRepository regionRepository;

    public void insert(SupportRequest request) {
        Region region = regionRepository.findByName(request.getRegion())
                .orElseThrow(() -> new IllegalStateException("해당 지자체가 존재하지 않습니다."));
        supportRepository.save(SupportRequest.toEntity(request, region));
    }

    public List<SupportResponse> search(String region) {
        Assert.hasText(region, "Required String parameter 'region' is empty");
        return supportRepository.findByList(region).stream()
                .map(SupportResponse::toEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    @CacheEvict(cacheNames = {"LimitAmountCacheData", "RateCacheData"}, allEntries = true)
    public void update(SupportRequest request) {
        List<Support> supports = supportRepository.findByList(request.getRegion());
        supports.forEach(support -> support.update(SupportRequest.toEntity(request, support.getRegion())));
    }

    @Cacheable(value="LimitAmountCacheData")
    public List<String> findByLimitAmountOrderByDesc(int size) {
        return getAll().stream()
                .sorted(Comparator.comparingLong(Support::getLimitAmount)
                        .reversed()
                        .thenComparing((o1, o2) -> {
                            double v1 = getAverageRate(o1);
                            double v2 = getAverageRate(o2);
                            return Double.compare(v1, v2);
                        })
                )
                .map(support -> support.getRegion().getName())
                .limit(size)
                .collect(Collectors.toList());
    }

    public List<Support> getAll() {
        return supportRepository.findAll();
    }

    @Cacheable(value="RateCacheData")
    public List<String> findBySuggestedInstitutionSmallestRate() {
        Map<Double, List<Support>> collect = getAll().stream()
                .collect(Collectors.groupingBy(this::getAverageRate))
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Double minRate = collect.keySet().stream()
                .min(Comparator.comparingDouble(o -> o))
                .orElse(0D);

        return collect.get(minRate).stream()
                .map(Support::getSuggestedInstitution)
                .collect(Collectors.toList());
    }

    private double getAverageRate(Support support) {
        return (support.getRate().getMinimum() + support.getRate().getMaximum()) / 2.0D;
    }
}
