package com.kakaopay.api.service;

import com.google.gson.Gson;
import com.kakaopay.api.domain.Support;
import com.kakaopay.api.domain.SupportRepository;
import com.kakaopay.api.domain.UseType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
class AgreementSupportServiceTest {
    private Stream<SupportRequest> supportRequestStream;

    @Autowired
    private AgreementSupportService agreementSupportService;

    @Autowired
    private SupportRepository supportRepository;

    @BeforeEach
    void setUp() {
        supportRequestStream = Stream.of(
                new Gson().fromJson("{ \"region\": \"횡성군\", \"target\": \"횡성군 소재 중소기업으로서 횡성군수의 융자 추천을 받은 자\", \"usage\": \"시설\", \"limit\" : \"100000000\", \"maximumRate\": \"6.33\", \"minimumRate\": \"4.00\",\"institute\": \"횡성군\", \"mgmt\": \"원주지점\", \"reception\": \"횡성군이 지정한 영업점\" }", SupportRequest.class),
                new Gson().fromJson("{\"region\": \"강릉시\", \"target\": \"강릉시 소재 중소기업으로서 강릉시장이 추천한 자\", \"usage\": \"운전\", \"limit\" : \"100\", \"maximumRate\": \"3.00\", \"minimumRate\": \"3.00\", \"institute\": \"강릉시\", \"mgmt\": \"강릉지점\",\"reception\": \"강릉시 소재 영업점\" }", SupportRequest.class)
        );
    }

    @AfterEach
    void tearDown() {
        supportRepository.deleteAll();
    }

    @Test
    void insert_data() {
        supportRequestStream.forEach(supportRequest -> agreementSupportService.insert(supportRequest));

        List<Support> all = supportRepository.findAll();

        assertThat(all.size(), is(2));
        assertAll("list value assertion..",
                ()->{
                    List<Support> supports = supportRepository.findByList("강릉시");
                    assertEquals(1, supports.size());
                    Support support = supports.stream().findFirst().orElseThrow(() -> new IllegalStateException("empty values.."));
                    assertEquals(UseType.DRIVE, support.getUseType());
                    assertEquals(3.0D, support.getRate().getMinimum());
                    assertEquals(3.0D, support.getRate().getMaximum());
                },
                ()->{
                    List<Support> supports = supportRepository.findByList("횡성군");
                    assertEquals(1, supports.size());
                    Support support = supports.stream().findFirst().orElseThrow(() -> new IllegalStateException("empty values.."));
                    assertEquals(UseType.FACILITY, support.getUseType());
                    assertEquals(4.0D, support.getRate().getMinimum());
                    assertEquals(6.33D, support.getRate().getMaximum());
                    assertEquals(100000000L, support.getLimitAmount());
                });
    }

    @Test
    void findByRegion() {
        supportRequestStream.forEach(supportRequest -> agreementSupportService.insert(supportRequest));

        List<SupportResponse> supportResponses = agreementSupportService.search("횡성군");

        assertThat(supportResponses.size(), is(1));
        assertAll("value assertion..",
                ()->{
                    SupportResponse support = supportResponses.stream().findFirst().orElseThrow(() -> new IllegalStateException("empty values.."));
                    assertEquals(UseType.FACILITY.getType(), support.getUsage());
                    assertEquals("4.0%~6.33%", support.getRate());
                    assertEquals("100,000,000 이내", support.getLimit());
                });

    }
}