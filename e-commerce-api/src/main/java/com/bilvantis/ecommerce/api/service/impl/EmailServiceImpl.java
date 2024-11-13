package com.bilvantis.ecommerce.api.service.impl;

import com.bilvantis.ecommerce.api.exception.ApplicationException;
import com.bilvantis.ecommerce.api.service.EmailService;
import com.bilvantis.ecommerce.api.util.ECommerceProperties;
import com.bilvantis.ecommerce.api.util.EmailDetails;
import com.bilvantis.ecommerce.api.util.EmailSupport;
import com.bilvantis.ecommerce.dao.data.model.User;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.nio.charset.StandardCharsets;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    private final ECommerceProperties eCommerceProperties;

    public EmailServiceImpl(JavaMailSender javaMailSender, ECommerceProperties eCommerceProperties) {
        this.javaMailSender = javaMailSender;
        this.eCommerceProperties = eCommerceProperties;
    }

    /**
     * Sends an OTP email to an employee using the provided email details.
     *
     * @param emailDetails EmailDetails
     * @param user         EmployeeEntity
     */

    @Override
    public void sendMailOtpGeneration(EmailDetails emailDetails, User user) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = EmailSupport.settingMimeMessageHelper(emailDetails, mimeMessage, eCommerceProperties.getSenderMailId());
            ClassPathResource emailTemplateResource = new ClassPathResource("otpEmail-Template.html");
            String emailTemplateContent = new String(FileCopyUtils.copyToByteArray(emailTemplateResource.getInputStream()), StandardCharsets.UTF_8);
            emailTemplateContent = emailTemplateContent.replace("${otp}", user.getOtp());
            emailTemplateContent = emailTemplateContent.replace("${name}", user.getFirstName());
            messageHelper.setText(emailTemplateContent, true);
            javaMailSender.send(mimeMessage);

        } catch (Exception e) {
            throw new ApplicationException(e.getMessage());
        }

    }

}
