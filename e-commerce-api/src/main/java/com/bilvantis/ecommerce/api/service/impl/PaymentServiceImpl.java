package com.bilvantis.ecommerce.api.service.impl;

import com.bilvantis.ecommerce.api.service.PaymentService;
import org.springframework.stereotype.Service;

@Service("paymentServiceImpl")
public class PaymentServiceImpl implements PaymentService {


    @Override
    public boolean processPayment(String orderId) {
        // Mock payment processing logic
        return Math.random() > 0.5;
    }
}
