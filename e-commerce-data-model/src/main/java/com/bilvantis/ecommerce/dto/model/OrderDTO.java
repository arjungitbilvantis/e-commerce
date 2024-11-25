package com.bilvantis.ecommerce.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO extends BaseDTO implements Serializable {
    private String orderId;
    private String userId;
    private String status;
    private String paymentStatus;
}
