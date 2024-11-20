package com.bilvantis.ecommerce.api.service;

import com.bilvantis.ecommerce.dto.model.InventoryDTO;

import java.io.Serializable;
import java.util.List;

public interface InventoryService<I extends InventoryDTO, ID extends Serializable> {
    I updateInventory(String productId, Integer quantity);

    I getInventoryByProductId(String productId);

    List<I> getAllInventories();
}
