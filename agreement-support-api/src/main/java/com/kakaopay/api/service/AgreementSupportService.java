package com.kakaopay.api.service;

import com.kakaopay.api.domain.Institution;
import com.kakaopay.api.domain.InstitutionRepository;
import com.kakaopay.api.domain.Support;
import com.kakaopay.api.domain.SupportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AgreementSupportService {

    private final SupportRepository supportRepository;
    private final InstitutionRepository institutionRepository;

    //insert
    public void insert(SupportRequest request) {
        Institution institution = institutionRepository.findByName(request.getRegion()).orElse(Institution.builder().build());
        supportRepository.save(SupportRequest.toEntity(request, institution));
    }

    //search & list
    public List<SupportResponse> search(String region) {
        Institution institution = institutionRepository.findByName(region).orElse(Institution.builder().build());
        List<Support> supports = supportRepository.findByList(institution.getName());
        return supports.stream()
                .map(SupportResponse::toEntity)
                .collect(Collectors.toList());
    }

    //update
    public void update(SupportRequest request) {
        Institution institution = institutionRepository.findByName(request.getRegion()).orElse(Institution.builder().build());
        List<Support> supports = supportRepository.findByList(institution.getName());

        supports.forEach(support -> support.update(SupportRequest.toEntity(request, institution)));
    }

    // desc sort limit
    public List<String> findByLimitAmountOrderByDesc(int size) {
        return supportRepository.findAll().stream()
                .sorted(Comparator.comparingLong(Support::getLimitAmount)
                        .reversed()
                        .thenComparing((o1, o2) -> {
                            double v1 = (o1.getRate().getMinimum() + o1.getRate().getMaximum()) / 2.0D;
                            double v2 = (o2.getRate().getMinimum() + o2.getRate().getMaximum()) / 2.0D;
                            return Double.compare(v1, v2);
                        })
                )
                .map(support -> support.getInstitution().getName())
                .limit(size)
                .collect(Collectors.toList());
    }
}
