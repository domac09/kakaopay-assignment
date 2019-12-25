package com.kakaopay.api.controller;

import com.kakaopay.api.service.AgreementSupportService;
import com.kakaopay.api.service.SupportRequest;
import com.kakaopay.api.service.SupportResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AgreementSupportController {

    private final AgreementSupportService agreementSupportService;

    @PostMapping("/v1/supports")
    public void insertData(@RequestBody SupportRequest supportRequest){
        agreementSupportService.insert(supportRequest);
    }

    @GetMapping("/v1/supports")
    public List<SupportResponse> getData(@RequestParam("region") String region){
        return agreementSupportService.search(region);
    }

    @PutMapping("/v1/supports")
    public void updateData(@RequestBody SupportRequest supportRequest){
        agreementSupportService.update(supportRequest);
    }

    @GetMapping("/v1/supports/limit")
    public List<String> getOrderByData(int size){
        return agreementSupportService.findByLimitAmountOrderByDesc(size);
    }

    @GetMapping("/v1/supports/rate")
    public List<String> getSmallestRateData(){
        return agreementSupportService.findBySuggestedInstitutionSmallestRate();
    }
}
