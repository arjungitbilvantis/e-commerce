package com.bilvantis.ecommerce.api.service.impl;

import com.bilvantis.ecommerce.api.service.PaymentService;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.bilvantis.ecommerce.api.util.PaymentConstants.SUCCESS_THRESHOLD;

@Service("paymentServiceImpl")
public class PaymentServiceImpl implements PaymentService {

    /**
     * Processes the payment for the given order ID.
     *
     * @param orderId The order ID for which the payment is to be processed.
     * @return {@code Boolean.TRUE} if the payment was successfully processed, {@code Boolean.FALSE} if the payment failed
     * or if the order ID is invalid.
     */
    @Override
    public Boolean processPayment(String orderId) {

        // Simulating validation of the order
        if (Objects.isNull(orderId) || orderId.isEmpty()) {
            return Boolean.FALSE;
        }

        // Simulating payment gateway interaction
        Boolean paymentGatewayResponse = simulatePaymentGatewayInteraction(orderId);

        if (!paymentGatewayResponse) {
            return Boolean.FALSE; // Payment failed
        }

        // Simulating payment success
        return Boolean.TRUE; // Payment successful
    }

    /**
     * Simulates interaction with a payment gateway.
     *
     * @param orderId The order ID for which payment is to be processed.
     * @return boolean indicating whether the payment gateway interaction was successful.
     */
    private Boolean simulatePaymentGatewayInteraction(String orderId) {
        // Simulates 50% chance of success
        return Math.random() > SUCCESS_THRESHOLD;
    }

    /**
     * Verifies the payment for the given order ID.
     *
     * @param orderId The order ID for which the payment is to be verified.
     * @return {@code Boolean.TRUE} if the payment is successful, {@code Boolean.FALSE} if the payment fails or is invalid.
     */
    @Override
    public Boolean verifyPayment(String orderId) {
        // Simulating the payment verification process
        return processPayment(orderId);
    }

}
