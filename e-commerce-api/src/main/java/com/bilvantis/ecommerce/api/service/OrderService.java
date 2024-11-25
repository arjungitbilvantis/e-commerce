package com.bilvantis.ecommerce.api.service;

import com.bilvantis.ecommerce.dto.model.OrderDTO;

import java.io.Serializable;

public interface OrderService<I extends OrderDTO, ID extends Serializable> {

    I  createOrder(I orderDTO);

    I updateOrderStatus(String orderId, String status);

    I getOrderDetails(String orderId);

    void checkPendingOrdersAndMarkFailed();
}
