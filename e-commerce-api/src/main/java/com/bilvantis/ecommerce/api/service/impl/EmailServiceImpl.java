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

            messageHelper.setText(emailTemplateContent, Boolean.TRUE);
            javaMailSender.send(mimeMessage);

        } catch (Exception e) {
            throw new ApplicationException(e.getMessage());
        }
    }

    /**
     * Sends a low-stock alert email to an administrator.
     *
     * @param emailDetails   EmailDetails containing recipient and subject details.
     * @param productId      ID of the product with low stock.
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

            messageHelper.setText(emailTemplateContent, Boolean.TRUE);
            javaMailSender.send(mimeMessage);

        } catch (Exception e) {
            throw new ApplicationException(LOW_STOCK_ALERT_EMAIL_FAILURE + e.getMessage());
        }
    }

    /**
     * Sends an order confirmation email to the user.
     *
     * @param emailDetails EmailDetails containing recipient and subject details.
     * @param user         The user who placed the order.
     * @param orderId      The order ID that was confirmed.
     */
    @Override
    public void sendOrderConfirmationEmail(EmailDetails emailDetails, User user, String orderId) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = settingMimeMessageHelper(emailDetails, mimeMessage, SENDER_EMAIL);

            // Load the order confirmation email template
            ClassPathResource emailTemplateResource = new ClassPathResource(ORDER_CONFIRMATION_EMAIL_TEMPLATE_PATH);
            String emailTemplateContent = new String(FileCopyUtils.copyToByteArray(emailTemplateResource.getInputStream()), StandardCharsets.UTF_8);

            // Replace placeholders with order-specific details
            emailTemplateContent = emailTemplateContent.replace(ORDER_ID_PLACEHOLDER, orderId);
            emailTemplateContent = emailTemplateContent.replace(USER_NAME_PLACEHOLDER, user.getFirstName());

            messageHelper.setText(emailTemplateContent, Boolean.TRUE);
            javaMailSender.send(mimeMessage);

        } catch (Exception e) {
            throw new ApplicationException(FAILED_TO_SEND_ORDER_CONFIRMATION_EMAIL + e.getMessage());
        }
    }

    /**
     * Sends an order failure email to the user.
     *
     * @param emailDetails EmailDetails containing recipient and subject details.
     * @param user         The user who placed the order.
     * @param orderId      The order ID that failed.
     */
    @Override
    public void sendOrderFailureEmail(EmailDetails emailDetails, User user, String orderId) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = settingMimeMessageHelper(emailDetails, mimeMessage, SENDER_EMAIL);

            // Load the order failure email template
            ClassPathResource emailTemplateResource = new ClassPathResource(ORDER_FAILURE_EMAIL_TEMPLATE_PATH);
            String emailTemplateContent = new String(FileCopyUtils.copyToByteArray(emailTemplateResource.getInputStream()), StandardCharsets.UTF_8);

            // Replace placeholders with order-specific details
            emailTemplateContent = emailTemplateContent.replace(ORDER_ID_PLACEHOLDER, orderId);
            emailTemplateContent = emailTemplateContent.replace(USER_NAME_PLACEHOLDER, user.getFirstName());

            messageHelper.setText(emailTemplateContent, Boolean.TRUE);
            javaMailSender.send(mimeMessage);

        } catch (Exception e) {
            throw new ApplicationException(FAILED_TO_SEND_ORDER_FAILURE_EMAIL + e.getMessage());
        }
    }

}
