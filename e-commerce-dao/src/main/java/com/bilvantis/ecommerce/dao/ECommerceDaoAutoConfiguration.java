package com.bilvantis.ecommerce.dao;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories
@EntityScan("com.bilvantis.ecommerce.dao.data.model")
public class ECommerceDaoAutoConfiguration {
}
