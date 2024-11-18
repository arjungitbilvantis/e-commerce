package com.bilvantis.ecommerce.api.service.impl;

import com.bilvantis.ecommerce.api.exception.ApplicationException;
import com.bilvantis.ecommerce.api.service.EmailService;
import com.bilvantis.ecommerce.api.util.EmailDetails;
import com.bilvantis.ecommerce.dao.data.model.User;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.nio.charset.StandardCharsets;

import static com.bilvantis.ecommerce.api.util.EmailConstants.*;
import static com.bilvantis.ecommerce.api.util.EmailSupport.settingMimeMessageHelper;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;


    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
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
            MimeMessageHelper messageHelper = settingMimeMessageHelper(emailDetails, mimeMessage, SENDER_EMAIL);

            ClassPathResource emailTemplateResource = new ClassPathResource(EMAIL_TEMPLATE_PATH);
            String emailTemplateContent = new String(FileCopyUtils.copyToByteArray(emailTemplateResource.getInputStream()), StandardCharsets.UTF_8);
            emailTemplateContent = emailTemplateContent.replace(OTP_PLACEHOLDER, user.getOtp());
            emailTemplateContent = emailTemplateContent.replace(NAME_PLACEHOLDER, user.getFirstName());

            messageHelper.setText(emailTemplateContent, true);
            javaMailSender.send(mimeMessage);

        } catch (Exception e) {
            throw new ApplicationException(e.getMessage());
        }
    }


}
