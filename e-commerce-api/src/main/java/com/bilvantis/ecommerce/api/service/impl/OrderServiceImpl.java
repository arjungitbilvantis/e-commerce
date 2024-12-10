package com.bilvantis.ecommerce.api.service.impl;

import com.bilvantis.ecommerce.api.exception.ApplicationException;
import com.bilvantis.ecommerce.api.service.*;
import com.bilvantis.ecommerce.api.util.EmailDetails;
import com.bilvantis.ecommerce.dao.data.model.Order;
import com.bilvantis.ecommerce.dao.data.model.OrderItem;
import com.bilvantis.ecommerce.dao.data.model.User;
import com.bilvantis.ecommerce.dao.data.repository.OrderItemRepository;
import com.bilvantis.ecommerce.dao.data.repository.OrderRepository;
import com.bilvantis.ecommerce.dao.data.repository.ProductRepository;
import com.bilvantis.ecommerce.dao.data.repository.UserRepository;
import com.bilvantis.ecommerce.dto.model.InventoryDTO;
import com.bilvantis.ecommerce.dto.model.OrderDTO;
import com.bilvantis.ecommerce.dto.model.OrderItemDTO;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static com.bilvantis.ecommerce.api.util.OrderConstants.*;
import static com.bilvantis.ecommerce.api.util.OrderSupport.convertOrderDTOToOrderEntity;
import static com.bilvantis.ecommerce.api.util.OrderSupport.convertOrderEntityToOrderDTO;
import static com.bilvantis.ecommerce.api.util.ProductConstants.PRODUCT_NOT_FOUND;
import static com.bilvantis.ecommerce.api.util.UserConstants.USER_NOT_FOUND;

@Service("orderServiceImpl")
@Slf4j
public class OrderServiceImpl implements OrderService<OrderDTO, String> {

    private final OrderRepository orderRepository;
    private final InventoryService<InventoryDTO, UUID> inventoryService;
    private final PaymentService paymentService;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final EmailService emailService;

    public OrderServiceImpl(OrderRepository orderRepository, InventoryService<InventoryDTO, UUID> inventoryService, PaymentService paymentService, UserRepository userRepository, ProductRepository productRepository, OrderItemRepository orderItemRepository, EmailService emailService) {
        this.orderRepository = orderRepository;
        this.inventoryService = inventoryService;
        this.paymentService = paymentService;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
        this.emailService = emailService;
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
            // Check if the user exists in the database
            User user = userRepository.findById(orderDTO.getUserId())  // Assuming OrderDTO has userId field
                    .orElseThrow(() -> new ApplicationException(String.format(USER_NOT_FOUND, orderDTO.getUserId())));

            // Convert List<OrderItemDTO> to Map<String, Integer>
            Map<String, Integer> itemsMap = orderDTO.getItems()
                    .stream()
                    .collect(Collectors.toMap(OrderItemDTO::getProductId, OrderItemDTO::getQuantity));

            // Check if each product exists in the database
            for (String productId : itemsMap.keySet()) {
                boolean productExists = productRepository.existsById(productId);
                if (!productExists) {
                    throw new ApplicationException(String.format(PRODUCT_NOT_FOUND, productId));
                }
            }

            // Check stock availability
            Boolean isStockAvailable = inventoryService.isStockAvailable(itemsMap);
            if (!isStockAvailable) {
                throw new ApplicationException(ORDER_OUT_OF_STOCK);
            }

            // Reserve stock
            inventoryService.reserveStock(itemsMap);

            // Create the order and set its properties
            Order order = createNewOrder(orderDTO, user);

            // Save the order
            Order savedOrder = orderRepository.save(order);

            // Save each order item to the order_items table
            saveOrderItems(savedOrder, orderDTO.getItems());

            return convertOrderEntityToOrderDTO(savedOrder);
        } catch (DataAccessException e) {
            log.error(ERROR_CREATING_ORDER, orderDTO.getUserId(), e.getMessage(), e);
            throw new ApplicationException(ORDER_CREATION_FAILED, e);
        }
    }

    /**
     * Helper method to create a new Order and set its properties.
     */
    private Order createNewOrder(OrderDTO orderDTO, User user) {
        Order order = convertOrderDTOToOrderEntity(orderDTO);
        Objects.requireNonNull(order).setOrderId(UUID.randomUUID().toString());
        order.setStatus(ORDER_STATUS_PENDING);
        order.setCreatedDate(new Date());
        order.setUserId(user.getUserId());
        return order;
    }

    /**
     * Saves the order items associated with a given order.
     *
     * @param savedOrder the order to which the items belong
     * @param items      the list of {@code OrderItemDTO} objects to be saved
     */
    private void saveOrderItems(Order savedOrder, List<OrderItemDTO> items) {
        for (OrderItemDTO itemDTO : items) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderItemId(UUID.randomUUID().toString());
            orderItem.setOrder(savedOrder);
            orderItem.setProductId(itemDTO.getProductId());
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItemRepository.save(orderItem);
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
            // Validate the status using the enum
            if (!OrderStatus.isValidStatus(status)) {
                throw new ApplicationException(String.format(INVALID_ORDER_STATUS, status));
            }

            // Fetch the existing order from the database
            Order order = orderRepository.findByOrderId(orderId)
                    .orElseThrow(() -> new ApplicationException(String.format(ORDER_NOT_FOUND, orderId)));

            // Fetch the associated user to validate their existence
            User user = userRepository.findById(order.getUserId())
                    .orElseThrow(() -> new ApplicationException(String.format(USER_NOT_FOUND, order.getUserId())));

            // If the status is "CONFIRMED", check payment status
            if (OrderStatus.CONFIRMED.getStatus().equals(status)) {
                Boolean isPaymentSuccessful = paymentService.verifyPayment(orderId);
                if (!isPaymentSuccessful) {
                    throw new ApplicationException(PAYMENT_FAILED);
                }
            }

            // If the status is "FAILED" or any other status, you might want to handle stock rollback or additional actions
            if (OrderStatus.FAILED.getStatus().equals(status)) {
                // Rollback stock if order failed
                inventoryService.rollbackStock(order);
            }

            // Update the order status
            order.setStatus(status);

            // Save the updated order
            orderRepository.save(order);

            // Send order confirmation or failure email based on the status
            if (OrderStatus.CONFIRMED.getStatus().equals(status)) {
                sendOrderConfirmationEmail(order); // Send order confirmation email
            } else if (OrderStatus.FAILED.getStatus().equals(status)) {
                sendOrderFailureEmail(order); // Send order failure email
            }

            // Return the updated order as a DTO
            return convertOrderEntityToOrderDTO(order);
        } catch (DataAccessException e) {
            log.error(ERROR_UPDATING_ORDER, orderId, e.getMessage(), e);
            throw new ApplicationException(String.format(ORDER_UPDATE_FAILED, orderId), e);
        }
    }

    /**
     * Sends an email to the user informing them about the confirmation of their order.
     *
     * @param order The order that has been confirmed
     */
    private void sendOrderConfirmationEmail(Order order) {
        try {
            // Fetch user details based on userId
            User user = userRepository.findById(order.getUserId())
                    .orElseThrow(() -> new ApplicationException("User not found"));

            // Compose email details
            String emailSubject = "Your Order Has Been Confirmed";
            String emailBody = String.format(
                    "Dear %s,\n\nWe are pleased to inform you that your order with ID %s has been successfully confirmed.\n\nThank you for shopping with us!\n\nBest regards,\nThe E-Commerce Team",
                    user.getFirstName(), order.getOrderId()
            );

            // Create an EmailDetails object to pass to the email service
//            EmailDetails emailDetails = new EmailDetails(user.getEmail(), emailSubject, emailBody);
            EmailDetails emailDetails = new EmailDetails();
            emailDetails.setRecipient(user.getEmail());
            emailDetails.setSubject(emailSubject);
            emailDetails.setMessageBody(emailBody);
            // Use the EmailService to send the email
            emailService.sendOrderConfirmationEmail(emailDetails, user, order.getOrderId());

            log.info("Order confirmation email sent successfully to {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send confirmation email for order {}: {}", order.getOrderId(), e.getMessage());
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
            log.error(ERROR_FETCHING_ORDER, orderId, e.getMessage(), e);
            throw new ApplicationException(String.format(ORDER_FETCH_FAILED, orderId), e);
        }
    }

    /**
     * Scheduled task that checks for pending orders and marks them as failed if they were created more than one day ago.
     *
     * @throws ApplicationException if there is an issue accessing the data
     */
    @Transactional
    @Override
    @Scheduled(cron = "0 0 0 * * *") // Run at midnight every day
    public void checkPendingOrdersAndMarkFailed() {

        try {
            // Get all orders that are currently pending
            List<Order> pendingOrders = orderRepository.findByStatus(ORDER_STATUS_PENDING);

            // Iterate through each pending order
            pendingOrders.forEach(order -> {
                try {
                    // Convert the createdDate (Date) to LocalDateTime
                    LocalDateTime createdDateTime = order.getCreatedDate().toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime();

                    // Check if the order was created more than 1 day ago
                    if (ChronoUnit.DAYS.between(createdDateTime, LocalDateTime.now()) > 1) {
                        // Mark the order as failed
                        order.setStatus(ORDER_STATUS_FAILED);
                        orderRepository.save(order);  // Save the updated order

                        // Send email to user notifying them about the failure
                        sendOrderFailureEmail(order);
                    }
                } catch (Exception e) {
                    log.error(ERROR_PENDING_ORDERS_UPDATE_FAILED, order.getOrderId(), e);
                }
            });
        } catch (DataAccessException e) {
            log.error(ERROR_PENDING_ORDERS_CHECK, e);
            throw new ApplicationException(ERROR_PENDING_ORDERS_CHECK_FAILED, e);
        }

    }

    /**
     * Sends an email to the user informing them about the failure of their order.
     *
     * @param order The order that has failed
     */
    private void sendOrderFailureEmail(Order order) {
        try {
            // Fetch user details based on userId
            User user = userRepository.findById(order.getUserId())
                    .orElseThrow(() -> new ApplicationException("User not found"));

            // Compose email details
            String emailSubject = "Your Order Has Failed";
            String emailBody = String.format(
                    "Dear %s,\n\nWe regret to inform you that your order with ID %s has failed because it was pending for more than 24 hours.\n\nWe apologize for the inconvenience caused.\n\nBest regards,\nThe E-Commerce Team",
                    user.getFirstName(), order.getOrderId()
            );

            // Create an EmailDetails object to pass to the email service
//            EmailDetails emailDetails = new EmailDetails(user.getEmail(), emailSubject, emailBody);
            EmailDetails emailDetails = new EmailDetails();
            emailDetails.setRecipient(user.getEmail());
            emailDetails.setSubject(emailSubject);
            emailDetails.setMessageBody(emailBody);
            // Use the EmailService to send the email
            emailService.sendOrderFailureEmail(emailDetails, user, order.getOrderId());

            log.info("Order failure email sent successfully to {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send failure email for order {}: {}", order.getOrderId(), e.getMessage());
        }
    }

}
