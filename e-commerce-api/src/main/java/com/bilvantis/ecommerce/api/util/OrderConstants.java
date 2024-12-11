package com.bilvantis.ecommerce.api.util;

public class OrderConstants {

    public static final String ORDER_STATUS_PENDING = "PENDING";
    public static final String ORDER_STATUS_FAILED = "FAILED";
    public static final String PAYMENT_STATUS_UNPAID = "UNPAID";
    public static final String ORDER_NOT_FOUND = "Order not found with ID: %s";
    public static final String ORDER_CREATION_FAILED = "Failed to create the order.";
    public static final String ORDER_UPDATE_FAILED = "Failed to update the order with ID: %s.";
    public static final String ORDER_FETCH_FAILED = "Failed to fetch the order with ID: %s.";
    public static final String PENDING_ORDERS_CHECK_FAILED = "Failed to process pending orders.";
    public static final String ORDER_OUT_OF_STOCK = "Order cannot be created as items are out of stock.";
    public static final String ORDER_STATUS_CONFIRMED = "CONFIRMED";
    public static final String PAYMENT_FAILED = "Payment failed for order: ";
    public static final String INVALID_ORDER_STATUS = "Invalid order status: %s";
    public static final String ERROR_CREATING_ORDER = "Error occurred while creating order for user ID: {}. Exception: {}";
    public static final String ERROR_UPDATING_ORDER = "Error occurred while updating order with ID: {}. Exception: {}";
    public static final String ERROR_FETCHING_ORDER = "Error occurred while fetching order with ID: {}. Exception: {}";
    public static final String ERROR_PENDING_ORDERS_CHECK = "Error occurred while checking pending orders.";
    public static final String ERROR_PENDING_ORDERS_UPDATE_FAILED = "Error occurred while updating pending order status to failed for order ID: {}";
    public static final String ERROR_PENDING_ORDERS_CHECK_FAILED = "Error occurred while checking and marking pending orders as failed.";
    public static final String ORDER_CONFIRMED_MESSAGE = "Your Order Has Been Confirmed";
    public static final String ORDER_CONFIRMATION_EMAIL_TEMPLATE =
            "Dear %s,\n\nWe are pleased to inform you that your order with ID %s has been successfully confirmed.\n\nThank you for shopping with us!\n\nBest regards,\nThe E-Commerce Team";
    public static final String CONFIRMATION_EMAIL_FAILURE_MESSAGE = "Failed to send confirmation email for order {}: {}";
    public static final String ORDER_FAILURE_MESSAGE = "Your Order Has Failed";
    public static final String ORDER_FAILURE_EMAIL_TEMPLATE =
            "Dear %s,\n\nWe regret to inform you that your order with ID %s has failed because it was pending for more than 24 hours.\n\nWe apologize for the inconvenience caused.\n\nBest regards,\nThe E-Commerce Team";
    public static final String FAILED_TO_SEND_FAILURE_EMAIL =
            "Failed to send failure email for order {}: {}";
}
