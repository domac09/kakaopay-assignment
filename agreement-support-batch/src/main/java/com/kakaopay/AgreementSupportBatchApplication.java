package com.kakaopay;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@EnableBatchProcessing
@SpringBootApplication
public class AgreementSupportBatchApplication {
    public static void main(String[] args) {
        int exit = SpringApplication.exit(SpringApplication.run(AgreementSupportBatchApplication.class, args));
        System.exit(exit);
    }
}
