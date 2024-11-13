package com.bilvantis.ecommerce.app.services.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ECommerceAppLogAOPConfig {

    @Around("execution (* com.bilvantis.ecommerce.api.service.*..*(..))")
    public Object logAroundMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        log.info("ECommerceAppLogAOP || {} method START", joinPoint.getSignature().getName());

        Object response = joinPoint.proceed();

        long endTime = System.currentTimeMillis();

        log.info("ECommerceAppLogAOP || {} method END", joinPoint.getSignature().getName());

        log.info("Total time taken for method: {} execution is {} seconds", joinPoint.getSignature().getName(), (endTime-startTime)/1000);

        return response;
    }
}
