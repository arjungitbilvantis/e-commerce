package com.bilvantis.ecommerce.api.util;

import com.bilvantis.ecommerce.dao.data.model.Order;
import com.bilvantis.ecommerce.dao.data.model.OrderItem;
import com.bilvantis.ecommerce.dto.model.OrderDTO;
import com.bilvantis.ecommerce.dto.model.OrderItemDTO;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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

        // Create a new OrderDTO and map the fields from the Order entity
        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setOrderId(order.getOrderId());
        orderDTO.setUserId(order.getUserId());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setPaymentStatus(order.getPaymentStatus());
        orderDTO.setIsActive(order.getIsActive());

        // Handle null items list by initializing as an empty list if needed
        List<OrderItemDTO> orderItemDTOList = Optional.ofNullable(order.getItems())
                .orElse(Collections.emptyList())  // If items is null, return an empty list
                .stream()
                .map(OrderSupport::convertOrderItemEntityToDTO)  // Assuming a helper method for converting OrderItem
                .collect(Collectors.toList());

        orderDTO.setItems(orderItemDTOList); // Set the items in the OrderDTO

        // Return the populated OrderDTO
        return orderDTO;
    }


    /**
     * Helper method to convert an OrderItem entity to an OrderItemDTO.
     *
     * @param orderItem The OrderItem entity to convert.
     * @return The corresponding OrderItemDTO.
     */
    private static OrderItemDTO convertOrderItemEntityToDTO(OrderItem orderItem) {
        if (Objects.isNull(orderItem)) {
            return null; // Return null if the OrderItem entity is null
        }

        // Create and populate the OrderItemDTO
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setOrderItemId(orderItem.getOrderItemId());
        orderItemDTO.setOrderId(orderItem.getOrder().getOrderId());
        orderItemDTO.setProductId(orderItem.getProductId());
        orderItemDTO.setQuantity(orderItem.getQuantity());

        return orderItemDTO;
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
