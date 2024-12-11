package com.bilvantis.ecommerce.api.util;

public class EmailConstants {

    public static final String SENDER_EMAIL = "Hr.policies@bilvantis.io";
    public static final String EMAIL_TEMPLATE_PATH = "otpEmail-Template.html";
    public static final String OTP_PLACEHOLDER = "${otp}";
    public static final String NAME_PLACEHOLDER = "${name}";
    public static final String LOW_STOCK_EMAIL_TEMPLATE_PATH = "low_stock_alert.html";
    public static final String PRODUCT_ID_PLACEHOLDER = "{{product_id}}";
    public static final String AVAILABLE_ITEMS_PLACEHOLDER = "{{available_items}}";
    public static final String LOW_STOCK_ALERT_EMAIL_FAILURE = "Failed to send low-stock alert email: ";

    // New constants for Order Confirmation and Failure Emails
    public static final String ORDER_CONFIRMATION_EMAIL_TEMPLATE_PATH = "order_confirmation_template.html";
    public static final String ORDER_FAILURE_EMAIL_TEMPLATE_PATH = "order_failure_template.html";

    // Placeholders for Order Confirmation and Failure Emails
    public static final String ORDER_ID_PLACEHOLDER = "${order_id}";
    public static final String USER_NAME_PLACEHOLDER = "${user_name}";
    public static final String FAILED_TO_SEND_ORDER_FAILURE_EMAIL = "Failed to send order failure email: ";
    public static final String FAILED_TO_SEND_ORDER_CONFIRMATION_EMAIL = "Failed to send order confirmation email: ";
}
