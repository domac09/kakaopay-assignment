package com.kakaopay;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class AgreementSupportApiApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(AgreementSupportApiApplication.class)
                .run(args);
    }
}
