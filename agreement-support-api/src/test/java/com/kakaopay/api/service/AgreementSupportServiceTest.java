package com.kakaopay.api.service;

import com.google.gson.Gson;
import com.kakaopay.api.domain.Support;
import com.kakaopay.api.domain.SupportRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
//@ExtendWith(value = {MockitoExtension.class, SpringExtension.class})
@SpringBootTest
class AgreementSupportServiceTest {

    @Autowired
    private AgreementSupportService agreementSupportService;

    @Autowired
    private SupportRepository supportRepository;

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
        supportRepository.deleteAll();
    }

    @Test
    void insert_data() {
        String request = "{\"region\": \"강릉시\", \"target\": \"강릉시 소재 중소기업으로서 강릉시장이 추천한 자\", \"usage\": \"운전\", \"limit\" : \"100\", \"maximumRate\": \"3.00\", \"minimumRate\": \"3.00\", \"institute\": \"강릉시\", \"mgmt\": \"강릉지점\",\"reception\": \"강릉시 소재 영업점\" }";

        SupportRequest supportDto = new Gson().fromJson(request, SupportRequest.class);

        agreementSupportService.insert(supportDto);

        List<Support> all = supportRepository.findAll();

        assertAll("value 비교",
                ()-> assertEquals(supportDto.getRegion(), all.get(0).getInstitution().getName()),
                ()-> assertEquals(supportDto.getMaximumRate(), all.get(0).getRate().getMaximum()),
                ()-> assertEquals(supportDto.getMinimumRate(), all.get(0).getRate().getMinimum()),
                ()-> assertEquals(supportDto.getLimit(), all.get(0).getLimitAmount())
        );
    }

    @Test
    void findByRegion() {
        String request = "{\"region\": \"강릉시\", \"target\": \"강릉시 소재 중소기업으로서 강릉시장이 추천한 자\", \"usage\": \"운전\", \"limit\" : \"100\", \"maximumRate\": \"3.00\", \"minimumRate\": \"3.00\", \"institute\": \"강릉시\", \"mgmt\": \"강릉지점\",\"reception\": \"강릉시 소재 영업점\" }";

        SupportRequest supportDto = new Gson().fromJson(request, SupportRequest.class);

        agreementSupportService.insert(supportDto);

        List<SupportResponse> supportResponses = agreementSupportService.search("강릉시");

        log.info("{}", supportResponses);
        assertThat(supportResponses.size(), is(1));
    }
}