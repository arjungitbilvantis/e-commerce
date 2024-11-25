package com.bilvantis.ecommerce.api.service.impl;

import com.bilvantis.ecommerce.api.service.EmailService;
import com.bilvantis.ecommerce.api.service.InventoryService;
import com.bilvantis.ecommerce.api.util.EmailDetails;
import com.bilvantis.ecommerce.dao.data.repository.InventoryRepository;
import com.bilvantis.ecommerce.dto.model.InventoryDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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
}
