package com.bilvantis.ecommerce.api.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Configuration
@ComponentScan
@PropertySource("classpath:ecommerce.properties")
public class ECommerceProperties {


    @Value("${login.id-password-mismatch}")
    private String userIdOrPasswordMismatch;

    @Value("${login.otp-sent-mail}")
    private String userOtpSentViaMail;

    @Value("${login.otp-Expired}")
    private String userOtpExpired;

    @Value("${login.otp-changed}")
    private String userOtpAlreadyChanged;

    @Value("${login.id-OTP-mismatch}")
    private String userOTPMismatch;

    @Value("${spring.mail.username}")
    private String senderMailId;

    @Value("${email.subject-otp-generation}")
    private String subjectForOtpGeneration;

    @Value("${login.otp-format-percentage}")
    private String otpFormatPercentage;

    @Value("${login.otp-format-numberofdigits-bound}")
    private String otpNoOfDigitsBound;
}
