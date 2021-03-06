package com.kakaopay.api.service;

import com.google.gson.Gson;
import com.kakaopay.api.domain.support.Support;
import com.kakaopay.api.domain.support.SupportRepository;
import com.kakaopay.api.domain.support.UseType;
import com.kakaopay.api.service.support.AgreementSupportService;
import com.kakaopay.api.service.support.SupportRequest;
import com.kakaopay.api.service.support.SupportResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
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
    @DisplayName("중소기업은행 지자체 협약 지원 정보 데이터를 입력한다.")
    void insert() {
        //given
        //when
        supportRequestStream.forEach(supportRequest -> agreementSupportService.insert(supportRequest));

        //then
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
    @DisplayName("지자체명을 입력 받아 해당 지자체의 지원정보를 출력한다.")
    void findByRegion() {
        //given
        supportRequestStream.forEach(supportRequest -> agreementSupportService.insert(supportRequest));

        //when
        List<SupportResponse> supportResponses = agreementSupportService.search("횡성군");

        //then
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
    @DisplayName("지자체명을 입력하지 않는 경우에는 exception 발생한다.")
    void searchByNull() {
        assertThrows(IllegalArgumentException.class,
                () -> {
                    supportRequestStream.forEach(supportRequest -> agreementSupportService.insert(supportRequest));
                    agreementSupportService.search("");
                });
    }

    @Test
    @DisplayName("중소기업은행 지자체 협약 지원 정보 데이터를 수정한다.")
    void update() {
        //given
        supportRequestStream.forEach(supportRequest -> agreementSupportService.insert(supportRequest));

        SupportRequest supportRequest = new Gson().fromJson("{ \"region\": \"횡성군\", \"target\": \"신의 선택을 받은 자\", \"usage\": \"운전 및 시설\", \"limit\" : \"1000\", \"maximumRate\": \"6.33\", \"minimumRate\": \"4.00\",\"institute\": \"횡성군\", \"mgmt\": \"원주지점\", \"reception\": \"신이 지정한 영업점\" }", SupportRequest.class);

        //when
        agreementSupportService.update(supportRequest);

        //then
        List<SupportResponse> supportResponses = agreementSupportService.search("횡성군");

        SupportResponse expectedResponse = new Gson().fromJson("{\"region\":\"횡성군\",\"target\":\"신의 선택을 받은 자\",\"limit\":\"1,000 이내\",\"rate\":\"4.0%~6.33%\",\"usage\":\"운전 및 시설\",\"institute\":\"횡성군\",\"mgmt\":\"원주지점\",\"reception\":\"신이 지정한 영업점\"}", SupportResponse.class);

        assertAll("update values test", () -> {
            assertEquals(expectedResponse.getTarget(), supportResponses.get(0).getTarget());
            assertEquals(expectedResponse.getUsage(), supportResponses.get(0).getUsage());
            assertEquals(expectedResponse.getReception(), supportResponses.get(0).getReception());
            assertEquals(expectedResponse.getLimit(), supportResponses.get(0).getLimit());
        });
    }

    //    지원한도 컬럼에서 지원금액으로 내림차순 정렬(지원금액이 동일하면 이차보전 평균 비율이 적은 순서)하여 특정 개수만 출력하는 API 개발
    //      입력:출력개수K
    //      출력: K 개의 지자체명 (e.g. { 강릉시, 강원도, 거제시, 경기도, 경상남도 } )
    @Test
    @DisplayName("지원금액으로 내림차순 정렬(지원금액이 동일하면 이차보전 평균 비율이 적은 순서)하여 특정 개수만 출력한다.")
    void findByLimitAmountOrderByDesc() {
        //given
        supportRequestStream.forEach(supportRequest -> agreementSupportService.insert(supportRequest));

        //when
        List<String> limitAmountOrderByDesc = agreementSupportService.findByLimitAmountOrderByDesc(3);

        //then
        assertThat(limitAmountOrderByDesc.size(), is(3));
        assertThat(Arrays.asList("거제시", "횡성군", "광명시"), is(limitAmountOrderByDesc));
    }

    @Test
    @DisplayName("이차보전 컬럼에서 보전 비율이 가장 작은 추천 기관명을 출력한다.")
    void findBySuggestedInstitutionSmallestRate() {
        //given
        supportRequestStream.forEach(supportRequest -> agreementSupportService.insert(supportRequest));

        //when
        List<String> bySuggestedInstitutionSmallestRate = agreementSupportService.findBySuggestedInstitutionSmallestRate();

        //then
        assertThat(Arrays.asList("광명시", "거제시, 경남신용보증재단"), is(bySuggestedInstitutionSmallestRate));
    }
}