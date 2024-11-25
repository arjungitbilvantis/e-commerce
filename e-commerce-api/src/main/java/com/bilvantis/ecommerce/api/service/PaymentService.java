package com.bilvantis.ecommerce.api.service;

public interface PaymentService {

    boolean processPayment(String orderId);
}
