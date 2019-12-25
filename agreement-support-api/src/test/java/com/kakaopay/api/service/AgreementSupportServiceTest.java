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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

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
                new Gson().fromJson("{\"region\": \"강릉시\", \"target\": \"강릉시 소재 중소기업으로서 강릉시장이 추천한 자\", \"usage\": \"운전\", \"limit\" : \"100\", \"maximumRate\": \"3.00\", \"minimumRate\": \"3.00\", \"institute\": \"강릉시\", \"mgmt\": \"강릉지점\",\"reception\": \"강릉시 소재 영업점\" }", SupportRequest.class),
                new Gson().fromJson("{\"region\": \"광명시\", \"target\": \"광명시 소재 중소기업으로서 광명시장이 추천한 자\", \"usage\": \"운전\", \"limit\" : \"100\", \"maximumRate\": \"2\", \"minimumRate\": \"2\", \"institute\": \"광명시\", \"mgmt\": \"광명지점\",\"reception\": \"전 영업점\" }", SupportRequest.class),
                new Gson().fromJson("{\"region\": \"거제시\", \"target\": \"거재시 소재 중소기업(소상공인 포함)으로서 거제시장이 추천한자\", \"usage\": \"운전 및 시설\", \"limit\" : \"100000000\", \"maximumRate\": \"2\", \"minimumRate\": \"2\", \"institute\": \"거제시, 경남신용보증재단\", \"mgmt\": \"거제지점\",\"reception\": \"거제시, 통영시에 소재영업점\" }", SupportRequest.class)
        );
    }

    @AfterEach
    void tearDown() {
        supportRepository.deleteAll();
    }

    @Test
    void insert() {
        supportRequestStream.forEach(supportRequest -> agreementSupportService.insert(supportRequest));

        List<Support> all = supportRepository.findAll();

        assertThat(all.size(), is(4));
        assertAll("list value assertion..",
                () -> {
                    List<Support> supports = supportRepository.findByList("강릉시");
                    assertEquals(1, supports.size());
                    Support support = supports.stream().findFirst().orElseThrow(() -> new IllegalStateException("empty values.."));
                    assertEquals(UseType.DRIVE, support.getUseType());
                    assertEquals(3.0D, support.getRate().getMinimum());
                    assertEquals(3.0D, support.getRate().getMaximum());
                },
                () -> {
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
                () -> {
                    SupportResponse support = supportResponses.stream().findFirst().orElseThrow(() -> new IllegalStateException("empty values.."));
                    assertEquals(UseType.FACILITY.getType(), support.getUsage());
                    assertEquals("4.0%~6.33%", support.getRate());
                    assertEquals("100,000,000 이내", support.getLimit());
                });

    }

    @Test
    void searchByNull() {
        assertThrows(IllegalArgumentException.class,
                () -> {
                    supportRequestStream.forEach(supportRequest -> agreementSupportService.insert(supportRequest));
                    List<SupportResponse> supportResponses = agreementSupportService.search("");
                });
    }

    @Test
    void update() {
        supportRequestStream.forEach(supportRequest -> agreementSupportService.insert(supportRequest));

        SupportRequest supportRequest = new Gson().fromJson("{ \"region\": \"횡성군\", \"target\": \"신의 선택을 받은 자\", \"usage\": \"운전 및 시설\", \"limit\" : \"1000\", \"maximumRate\": \"6.33\", \"minimumRate\": \"4.00\",\"institute\": \"횡성군\", \"mgmt\": \"원주지점\", \"reception\": \"신이 지정한 영업점\" }", SupportRequest.class);

        agreementSupportService.update(supportRequest);

        List<SupportResponse> supportResponses = agreementSupportService.search("횡성군");

        List<SupportResponse> expectedResponses = Collections.singletonList(new Gson().fromJson("{\"region\":\"횡성군\",\"target\":\"신의 선택을 받은 자\",\"limit\":\"1,000 이내\",\"rate\":\"4.0%~6.33%\",\"usage\":\"운전 및 시설\",\"institute\":\"횡성군\",\"mgmt\":\"원주지점\",\"reception\":\"신이 지정한 영업점\"}", SupportResponse.class));

        assertAll("update values test", () -> {
            assertEquals(expectedResponses.get(0).getTarget(), supportResponses.get(0).getTarget());
            assertEquals(expectedResponses.get(0).getUsage(), supportResponses.get(0).getUsage());
            assertEquals(expectedResponses.get(0).getReception(), supportResponses.get(0).getReception());
            assertEquals(expectedResponses.get(0).getLimit(), supportResponses.get(0).getLimit());
        });
    }

    //    지원한도 컬럼에서 지원금액으로 내림차순 정렬(지원금액이 동일하면 이차보전 평균 비율이 적은 순서)하여 특정 개수만 출력하는 API 개발
//      입력:출력개수K
//      출력: K 개의 지자체명 (e.g. { 강릉시, 강원도, 거제시, 경기도, 경상남도 } )
    @Test
    void findByLimitAmountOrderByDesc() {
        supportRequestStream.forEach(supportRequest -> agreementSupportService.insert(supportRequest));

        List<String> limitAmountOrderByDesc = agreementSupportService.findByLimitAmountOrderByDesc(3);

        List<String> expectedList = Arrays.asList("거제시", "횡성군", "광명시");

        assertThat(limitAmountOrderByDesc.size(), is(3));
        assertThat(expectedList, is(limitAmountOrderByDesc));
    }

    @Test
    void findBySuggestedInstitutionSmallestRate() {
        supportRequestStream.forEach(supportRequest -> agreementSupportService.insert(supportRequest));

        List<String> bySuggestedInstitutionSmallestRate = agreementSupportService.findBySuggestedInstitutionSmallestRate();

        List<String> expectedList = Arrays.asList("광명시", "거제시, 경남신용보증재단");

        assertThat(expectedList, is(bySuggestedInstitutionSmallestRate));
    }
}