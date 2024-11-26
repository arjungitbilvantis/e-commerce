package com.bilvantis.ecommerce.api.service;

public enum OrderStatus {

    PENDING("PENDING"),
    FAILED("FAILED"),
    CONFIRMED("CONFIRMED");

    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    // Optionally, you can add a method to check if a status is valid
    public static Boolean isValidStatus(String status) {
        for (OrderStatus orderStatus : values()) {
            if (orderStatus.getStatus().equals(status)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
}
