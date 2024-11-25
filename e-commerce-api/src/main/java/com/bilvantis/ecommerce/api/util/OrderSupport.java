package com.bilvantis.ecommerce.api.util;

import com.bilvantis.ecommerce.dao.data.model.Order;
import com.bilvantis.ecommerce.dto.model.OrderDTO;

import java.util.Objects;

public class OrderSupport {

    /**
     * Converts an Order entity to an OrderDTO object.
     *
     * @param order the Order entity containing the order data
     * @return an OrderDTO object populated with the data from the Order entity
     */
    public static OrderDTO convertOrderEntityToOrderDTO(Order order) {
        if (Objects.isNull(order)) {
            return null;
        }

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(order.getOrderId());
        orderDTO.setUserId(order.getUserId());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setPaymentStatus(order.getPaymentStatus());

        // Populate fields from BaseEntity if applicable
        orderDTO.setIsActive(order.getIsActive());
        orderDTO.setCreatedBy(order.getCreatedBy());
        orderDTO.setCreatedDate(order.getCreatedDate());
        orderDTO.setUpdatedBy(order.getUpdatedBy());
        orderDTO.setUpdatedDate(order.getUpdatedDate());

        return orderDTO;
    }

    /**
     * Converts an OrderDTO object to an Order entity.
     *
     * @param orderDTO the OrderDTO object containing the order data
     * @return an Order entity populated with the data from the OrderDTO
     */
    public static Order convertOrderDTOToOrderEntity(OrderDTO orderDTO) {
        if (Objects.isNull(orderDTO)) {
            return null;
        }

        Order order = new Order();
        order.setOrderId(orderDTO.getOrderId());
        order.setUserId(orderDTO.getUserId());
        order.setStatus(orderDTO.getStatus());
        order.setPaymentStatus(orderDTO.getPaymentStatus());

        // Populate fields from BaseDTO if applicable
        order.setIsActive(Boolean.TRUE);
        order.setCreatedBy(orderDTO.getCreatedBy());
        order.setCreatedDate(orderDTO.getCreatedDate());
        order.setUpdatedBy(orderDTO.getUpdatedBy());
        order.setUpdatedDate(orderDTO.getUpdatedDate());

        return order;
    }

    /**
     * Updates the fields of an existing Order entity based on the values from an OrderDTO object.
     *
     * @param existingOrder the Order entity to be updated
     * @param orderDTO      the OrderDTO object containing the new values
     */
    public static void updateOrderFromDTO(Order existingOrder, OrderDTO orderDTO) {
        if (Objects.isNull(existingOrder) || Objects.isNull(orderDTO)) {
            return;
        }

        existingOrder.setStatus(orderDTO.getStatus());
        existingOrder.setPaymentStatus(orderDTO.getPaymentStatus());
        existingOrder.setUpdatedBy(orderDTO.getUpdatedBy());
        existingOrder.setUpdatedDate(orderDTO.getUpdatedDate());
    }
}
