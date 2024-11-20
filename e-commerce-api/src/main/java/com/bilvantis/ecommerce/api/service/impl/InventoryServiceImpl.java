package com.bilvantis.ecommerce.api.service.impl;

import com.bilvantis.ecommerce.api.service.InventoryService;
import com.bilvantis.ecommerce.dto.model.InventoryDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service("inventoryServiceImpl")
public class InventoryServiceImpl implements InventoryService<InventoryDTO, UUID> {


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
}
