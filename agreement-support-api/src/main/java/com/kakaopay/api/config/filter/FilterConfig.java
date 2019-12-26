package com.kakaopay.api.config.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class FilterConfig {
    @Value("${secret-key}")
    private String secretKey;

    @Bean
    public FilterRegistrationBean<AuthenticationFilter> authenticationFilter() {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(secretKey);

        FilterRegistrationBean<AuthenticationFilter> filter = new FilterRegistrationBean<>(authenticationFilter);
        filter.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);

        return filter;
    }
}
