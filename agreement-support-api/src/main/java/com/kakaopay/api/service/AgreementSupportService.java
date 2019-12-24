package com.kakaopay.api.service;

import com.kakaopay.api.domain.Institution;
import com.kakaopay.api.domain.InstitutionRepository;
import com.kakaopay.api.domain.Support;
import com.kakaopay.api.domain.SupportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AgreementSupportService {

    private final SupportRepository supportRepository;
    private final InstitutionRepository institutionRepository;

    //insert
    public void insert(SupportRequest request){
        Institution institution = institutionRepository.findByName(request.getRegion()).orElse(Institution.builder().build());
        supportRepository.save(SupportRequest.toEntity(request, institution));
    }

    //search & list
    public List<SupportResponse> search(String region){
        Institution institution = institutionRepository.findByName(region).orElse(Institution.builder().build());
        List<Support> supports = supportRepository.findByList(institution.getName());
        return supports.stream()
                .map(support -> SupportResponse.toEntity(support, institution))
                .collect(Collectors.toList());
    }

    //update

    // search by institute

    // desc sort limit
}
