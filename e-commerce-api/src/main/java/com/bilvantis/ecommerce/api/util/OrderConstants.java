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
}
