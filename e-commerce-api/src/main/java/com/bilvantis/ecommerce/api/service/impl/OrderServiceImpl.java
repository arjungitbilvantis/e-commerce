package com.bilvantis.ecommerce.api.service.impl;

import com.bilvantis.ecommerce.api.exception.ApplicationException;
import com.bilvantis.ecommerce.api.service.OrderService;
import com.bilvantis.ecommerce.dao.data.model.Order;
import com.bilvantis.ecommerce.dao.data.repository.OrderRepository;
import com.bilvantis.ecommerce.dto.model.OrderDTO;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.bilvantis.ecommerce.api.util.OrderConstants.*;
import static com.bilvantis.ecommerce.api.util.OrderSupport.convertOrderDTOToOrderEntity;
import static com.bilvantis.ecommerce.api.util.OrderSupport.convertOrderEntityToOrderDTO;

@Service("orderServiceImpl")
public class OrderServiceImpl implements OrderService<OrderDTO, String> {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Creates a new order for the given userId.
     *
     * @param orderDTO the ID of the user
     * @return the ID of the created order
     * @throws ApplicationException if the save operation fails
     */
    @Transactional
    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {
        try {
            // Create a new order entity from the DTO
            Order order = convertOrderDTOToOrderEntity(orderDTO);

            // Generate a random UUID for orderId before saving
            UUID generatedOrderId = UUID.randomUUID();
            Objects.requireNonNull(order).setOrderId(generatedOrderId.toString());

            // Set default status and payment status if not provided
            order.setStatus(orderDTO.getStatus() != null ? orderDTO.getStatus() : ORDER_STATUS_PENDING);
            order.setPaymentStatus(orderDTO.getPaymentStatus() != null ? orderDTO.getPaymentStatus() : PAYMENT_STATUS_UNPAID);

            // Set the created date
            order.setCreatedDate(new Date());

            // Save the order entity
            Order savedOrder = orderRepository.save(order);

            return convertOrderEntityToOrderDTO(savedOrder);
        } catch (DataAccessException e) {
            throw new ApplicationException(ORDER_CREATION_FAILED, e);
        }
    }

    /**
     * Updates the status of an order.
     *
     * @param orderId the ID of the order to update
     * @param status  the new status
     * @return the updated OrderDTO
     * @throws ApplicationException if the order is not found or update fails
     */
    @Transactional
    @Override
    public OrderDTO updateOrderStatus(String orderId, String status) {
        try {
            Order order = orderRepository.findByOrderId(orderId)
                    .orElseThrow(() -> new ApplicationException(String.format(ORDER_NOT_FOUND, orderId)));

            order.setStatus(status);
            orderRepository.save(order);

            return convertOrderEntityToOrderDTO(order);
        } catch (DataAccessException e) {
            throw new ApplicationException(String.format(ORDER_UPDATE_FAILED, orderId), e);
        }
    }

    /**
     * Retrieves the details of an order by ID.
     *
     * @param orderId the ID of the order to retrieve
     * @return the OrderDTO object
     * @throws ApplicationException if the order is not found or fetch fails
     */
    @Override
    public OrderDTO getOrderDetails(String orderId) {
        try {
            Order order = orderRepository.findByOrderId(orderId)
                    .orElseThrow(() -> new ApplicationException(String.format(ORDER_NOT_FOUND, orderId)));

            return convertOrderEntityToOrderDTO(order);
        } catch (DataAccessException e) {
            throw new ApplicationException(String.format(ORDER_FETCH_FAILED, orderId), e);
        }
    }

    /**
     * Checks for pending orders older than 1 day and marks them as failed.
     *
     * @throws ApplicationException if the operation fails
     */
    @Transactional
    @Override
    public void checkPendingOrdersAndMarkFailed() {
        try {
            List<Order> pendingOrders = orderRepository.findByStatus(ORDER_STATUS_PENDING);

            pendingOrders.forEach(order -> {
                if (ChronoUnit.DAYS.between((Temporal) order.getCreatedDate(), LocalDateTime.now()) > 1) {
                    order.setStatus(ORDER_STATUS_FAILED);
                    orderRepository.save(order);
                }
            });
        } catch (DataAccessException e) {
            throw new ApplicationException(PENDING_ORDERS_CHECK_FAILED, e);
        }
    }
}
