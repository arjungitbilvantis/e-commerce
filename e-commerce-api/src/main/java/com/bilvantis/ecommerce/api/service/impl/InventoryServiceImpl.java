package com.bilvantis.ecommerce.api.service.impl;

import com.bilvantis.ecommerce.api.service.EmailService;
import com.bilvantis.ecommerce.api.service.InventoryService;
import com.bilvantis.ecommerce.api.util.EmailDetails;
import com.bilvantis.ecommerce.dao.data.model.Inventory;
import com.bilvantis.ecommerce.dao.data.model.Order;
import com.bilvantis.ecommerce.dao.data.model.OrderItem;
import com.bilvantis.ecommerce.dao.data.repository.InventoryRepository;
import com.bilvantis.ecommerce.dto.model.InventoryDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.bilvantis.ecommerce.api.util.InventoryConstants.LOW_STOCK_ALERT_SUBJECT;

@Service("inventoryServiceImpl")
public class InventoryServiceImpl implements InventoryService<InventoryDTO, UUID> {

    @Value("${admin.email}")
    private String adminEmail;
    private final InventoryRepository inventoryRepository;

    private final EmailService emailService;

    public InventoryServiceImpl(InventoryRepository inventoryRepository, EmailService emailService) {
        this.inventoryRepository = inventoryRepository;
        this.emailService = emailService;
    }

    @Override
    public InventoryDTO updateInventory(String productId, Integer quantity) {
        return null;
    }

    @Override
    public InventoryDTO getInventoryByProductId(String productId) {
        return null;
    }

    @Override
    public List<InventoryDTO> getAllInventories() {
        return null;
    }

    @Override
    @Scheduled(cron = "0 */30 * * * *") // Run every 30minutes
    public void monitorLowStockItems() {
        inventoryRepository.findByAvailableItemsLessThanEqual(2)
                .forEach(inventory -> {
                    EmailDetails emailDetails = new EmailDetails();
                    emailDetails.setRecipient(adminEmail);
                    emailDetails.setSubject(LOW_STOCK_ALERT_SUBJECT + inventory.getProductId());
                    emailService.sendLowStockAlert(emailDetails, inventory.getProductId(), inventory.getAvailableItems());
                });
    }

    /**
     * Checks if the stock is available for the given items.
     *
     * @param items A map where the key is the product ID and the value is the quantity of the product being requested.
     * @return {@code true} if the available stock is sufficient for all products in the map, {@code false} otherwise.
     */
    @Override
    public Boolean isStockAvailable(Map<String, Integer> items) {
        return items.entrySet().stream()
                .allMatch(entry -> {
                    String productId = entry.getKey();
                    Integer quantityRequested = entry.getValue();

                    return inventoryRepository.findByProductId(productId)
                            .map(inventory -> inventory.getAvailableItems() >= quantityRequested)
                            .orElse(Boolean.FALSE); // If inventory is not found, return false
                });
    }

    /**
     * Reserves stock for a given list of items if available.
     *
     * @param items A map where the key is the product ID, and the value is the quantity to reserve.
     * @return true if all items were successfully reserved; false if any item was not available.
     */
    @Override
    public Boolean reserveStock(Map<String, Integer> items) {
        return items.entrySet().stream()
                .allMatch(entry -> {
                    String productId = entry.getKey();
                    Integer quantityToReserve = entry.getValue();

                    // Check availability and update the stock atomically using optimistic locking
                    Optional<Inventory> inventoryOpt = inventoryRepository.findByProductId(productId);
                    if (inventoryOpt.isEmpty()) {
                        return Boolean.FALSE;  // Product doesn't exist
                    }

                    Inventory inventory = inventoryOpt.get();

                    if (Objects.isNull(inventory.getReservedItems())) {
                        inventory.setReservedItems(0);
                    }

                    if (inventory.getAvailableItems() < quantityToReserve) {
                        return Boolean.FALSE;  // Not enough stock
                    }

                    // Update the stock: decrement available items and increment reserved items
                    inventory.setAvailableItems(inventory.getAvailableItems() - quantityToReserve);
                    inventory.setReservedItems(inventory.getReservedItems() + quantityToReserve);

                    // Save the updated inventory and handle optimistic locking
                    try {
                        inventoryRepository.save(inventory);
                        return Boolean.TRUE;
                    } catch (OptimisticLockingFailureException e) {
                        return Boolean.FALSE;
                    }
                });
    }

    /**
     * Rolls back the stock levels for the items in the given order.
     *
     * @param order the order containing the items to roll back
     * @return {@code true} if all stock updates were successful, {@code false} otherwise
     */
    @Override
    public Boolean rollbackStock(Order order) {
        // Loop through all items in the order and update stock levels
        for (OrderItem orderItem : order.getItems()) {
            String productId = orderItem.getProductId();
            Integer quantityReserved = orderItem.getQuantity();

            Optional<Inventory> inventoryOpt = inventoryRepository.findByProductId(productId);
            if (inventoryOpt.isEmpty()) {
                continue;  // If inventory doesn't exist for the product, skip it
            }

            Inventory inventory = inventoryOpt.get();

            // If there are reserved items, return them back to available stock
            if (inventory.getReservedItems() >= quantityReserved) {
                inventory.setReservedItems(inventory.getReservedItems() - quantityReserved); // Undo reservation
                inventory.setAvailableItems(inventory.getAvailableItems() + quantityReserved); // Add back to available stock

                // Save the updated inventory
                try {
                    inventoryRepository.save(inventory);
                } catch (OptimisticLockingFailureException e) {
                    return Boolean.FALSE; // If there was a concurrency issue, return false
                }
            } else {
                return Boolean.FALSE; // If there was not enough reserved stock to rollback
            }
        }
        return Boolean.TRUE; // If all stock updates were successful
    }


}
