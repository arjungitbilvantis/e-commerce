package com.bilvantis.ecommerce.app;

import com.bilvantis.ecommerce.api.ECommerceServiceAutoConfiguration;
import com.bilvantis.ecommerce.dao.ECommerceDaoAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@ComponentScan(basePackages={
        "com.bilvantis.ecommerce.app.*",
})
@Import({ECommerceDaoAutoConfiguration.class, ECommerceServiceAutoConfiguration.class})
public class ECommerceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ECommerceApplication.class, args);
    }
}