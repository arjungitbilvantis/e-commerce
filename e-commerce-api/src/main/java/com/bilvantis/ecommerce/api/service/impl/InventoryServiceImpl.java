package com.bilvantis.ecommerce.api.service.impl;

import com.bilvantis.ecommerce.api.service.EmailService;
import com.bilvantis.ecommerce.api.service.InventoryService;
import com.bilvantis.ecommerce.api.util.EmailDetails;
import com.bilvantis.ecommerce.dao.data.model.Inventory;
import com.bilvantis.ecommerce.dao.data.repository.InventoryRepository;
import com.bilvantis.ecommerce.dto.model.InventoryDTO;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service("inventoryServiceImpl")
public class InventoryServiceImpl implements InventoryService<InventoryDTO, UUID> {

    private static final String ADMIN_EMAIL = "mallikharjuna.pula@bilvantis.io";
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
    @Scheduled(cron = "0 */2 * * * *") // Run every 2min
    public void monitorLowStockItems() {
        List<Inventory> lowStockInventories = inventoryRepository.findByAvailableItemsLessThanEqual(2);

        for (Inventory inventory : lowStockInventories) {
            EmailDetails emailDetails = new EmailDetails();
            emailDetails.setRecipient(ADMIN_EMAIL);
            emailDetails.setSubject("Low Stock Alert for Product ID: " + inventory.getProductId());

            emailService.sendLowStockAlert(emailDetails, inventory.getProductId(), inventory.getAvailableItems());
        }

    }
}
