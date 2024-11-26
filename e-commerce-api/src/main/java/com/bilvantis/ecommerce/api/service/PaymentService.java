package com.bilvantis.ecommerce.api.service;

public interface PaymentService {

    Boolean processPayment(String orderId);
    Boolean verifyPayment(String orderId);
}
