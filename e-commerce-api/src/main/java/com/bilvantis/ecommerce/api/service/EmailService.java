package com.bilvantis.ecommerce.api.service;


import com.bilvantis.ecommerce.api.util.EmailDetails;
import com.bilvantis.ecommerce.dao.data.model.User;

public interface EmailService {
    void sendMailOtpGeneration(EmailDetails emailDetails, User user);
    void sendLowStockAlert(EmailDetails emailDetails, String productId, int availableItems);

}
