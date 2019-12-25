package com.kakaopay.api.controller;

import com.kakaopay.api.service.support.AgreementSupportService;
import com.kakaopay.api.service.support.SupportRequest;
import com.kakaopay.api.service.support.SupportResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/supports")
@RequiredArgsConstructor
public class AgreementSupportController {

    private final AgreementSupportService agreementSupportService;

    @PostMapping
    public void insertData(@RequestBody SupportRequest supportRequest){
        agreementSupportService.insert(supportRequest);
    }

    @GetMapping
    public List<SupportResponse> getData(@RequestParam("region") String region){
        return agreementSupportService.search(region);
    }

    @PutMapping
    public void updateData(@RequestBody SupportRequest supportRequest){
        agreementSupportService.update(supportRequest);
    }

    @GetMapping("/limit")
    public List<String> getOrderByData(int size){
        return agreementSupportService.findByLimitAmountOrderByDesc(size);
    }

    @GetMapping("/rate")
    public List<String> getSmallestRateData(){
        return agreementSupportService.findBySuggestedInstitutionSmallestRate();
    }
}
