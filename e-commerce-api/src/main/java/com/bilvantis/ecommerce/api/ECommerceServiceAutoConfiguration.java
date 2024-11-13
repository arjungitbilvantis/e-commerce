package com.bilvantis.ecommerce.api;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ComponentScan(basePackages = {
        "com.bilvantis.ecommerce.api.*",
})
public class ECommerceServiceAutoConfiguration {
}
