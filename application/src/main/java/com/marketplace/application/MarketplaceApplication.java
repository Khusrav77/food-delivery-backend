package com.marketplace.application;

import com.marketplace.identity.infrastructure.security.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(JwtProperties.class)
@SpringBootApplication(scanBasePackages = "com.marketplace")
public class MarketplaceApplication {

     static void main(String[] args) {
        SpringApplication.run(MarketplaceApplication.class, args);
    }
}