package com.bilvantis.ecommerce.controller;

import com.bilvantis.ecommerce.api.service.OrderService;
import com.bilvantis.ecommerce.dto.model.OrderDTO;
import com.bilvantis.ecommerce.dto.util.OnCreate;
import com.bilvantis.ecommerce.model.UserResponseDTO;
import com.bilvantis.ecommerce.util.ECommerceAppConstant;
import com.bilvantis.ecommerce.util.UserRequestResponseBuilder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/order")
public class OrderController {


    private final OrderService<OrderDTO, String> orderService;

    public OrderController(OrderService<OrderDTO, String> orderService) {
        this.orderService = orderService;
    }

    /**
     * Creates a new order based on the provided OrderDTO.
     *
     * @param orderDTO the OrderDTO object containing the order data
     * @return a ResponseEntity containing the created OrderDTO and HTTP status code 201 (Created)
     */
    @Validated(OnCreate.class)
    @PostMapping
    public ResponseEntity<UserResponseDTO> createOrder(@NotNull @Valid @RequestBody OrderDTO orderDTO) {
        return new ResponseEntity<>(UserRequestResponseBuilder.buildResponseDTO(
                orderService.createOrder(orderDTO), null, ECommerceAppConstant.SUCCESS),
                HttpStatus.CREATED);
    }

    /**
     * Updates the status of an order.
     *
     * @param orderId the ID of the order to update
     * @param status  the new status
     * @return the updated order details in response
     */
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<UserResponseDTO> updateOrderStatus(@PathVariable String orderId,
                                                             @RequestParam String status) {
        OrderDTO updatedOrder = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(UserRequestResponseBuilder.buildResponseDTO(
                updatedOrder, null, ECommerceAppConstant.SUCCESS));
    }

    /**
     * Retrieves the details of an order by ID.
     *
     * @param orderId the ID of the order to retrieve
     * @return the order details in response
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<UserResponseDTO> getOrderDetails(@PathVariable String orderId) {
        OrderDTO orderDetails = orderService.getOrderDetails(orderId);
        return ResponseEntity.ok(UserRequestResponseBuilder.buildResponseDTO(
                orderDetails, null, ECommerceAppConstant.SUCCESS));
    }

    /**
     * Checks for pending orders older than 1 day and marks them as failed.
     *
     * @return a success message
     */
    @PostMapping("/checkPendingOrders")
    public ResponseEntity<UserResponseDTO> checkPendingOrdersAndMarkFailed() {
        orderService.checkPendingOrdersAndMarkFailed();
        return ResponseEntity.ok(UserRequestResponseBuilder.buildResponseDTO(
                null, null, "Pending orders check completed successfully."));
    }


}
