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

    /**
     * Sends a low-stock alert email to an administrator.
     *
     * @param emailDetails EmailDetails containing recipient and subject details.
     * @param productId    ID of the product with low stock.
     * @param availableItems Current stock quantity of the product.
     */
    @Override
    public void sendLowStockAlert(EmailDetails emailDetails, String productId, int availableItems) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = settingMimeMessageHelper(emailDetails, mimeMessage, SENDER_EMAIL);

            // Load the low-stock email template
            ClassPathResource emailTemplateResource = new ClassPathResource(LOW_STOCK_EMAIL_TEMPLATE_PATH);
            String emailTemplateContent = new String(FileCopyUtils.copyToByteArray(emailTemplateResource.getInputStream()), StandardCharsets.UTF_8);

            // Replace placeholders with product-specific details
            emailTemplateContent = emailTemplateContent.replace(PRODUCT_ID_PLACEHOLDER, productId);
            emailTemplateContent = emailTemplateContent.replace(AVAILABLE_ITEMS_PLACEHOLDER, String.valueOf(availableItems));

            messageHelper.setText(emailTemplateContent, true);
            javaMailSender.send(mimeMessage);

        } catch (Exception e) {
            throw new ApplicationException("Failed to send low-stock alert email: " + e.getMessage());
        }
    }



}
