package com.bilvantis.ecommerce.api.util;


import com.bilvantis.ecommerce.api.exception.ApplicationException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.nio.charset.StandardCharsets;

public class EmailSupport {

    /**
     * Sets up a MimeMessageHelper with the provided email details and sender email ID.
     *
     * @param emailDetails the details of the email including recipient and subject
     * @param mimeMessage  the MimeMessage object to be configured
     * @param senderMailId the email ID of the sender
     * @return a configured MimeMessageHelper object
     * @throws MessagingException if there is an error while setting up the MimeMessageHelper
     */
    public static MimeMessageHelper settingMimeMessageHelper(EmailDetails emailDetails, MimeMessage mimeMessage, String senderMailId) throws MessagingException {
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.toString());
            messageHelper.setFrom(senderMailId);
            messageHelper.setTo(emailDetails.getRecipient());
            messageHelper.setSubject(emailDetails.getSubject());
            return messageHelper;
        } catch (MessagingException e) {
            throw new ApplicationException(e.getMessage());
        }
    }

    /**
     * Creates and sets up an EmailDetails object with the provided recipient email ID and subject.
     *
     * @param mailId  the email ID of the recipient
     * @param subject the subject of the email
     * @return a configured EmailDetails object
     */
    public static EmailDetails settingEmailDetails(String mailId, String subject) {
        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setRecipient(mailId);
        emailDetails.setSubject(subject);
        return emailDetails;

    }


}
