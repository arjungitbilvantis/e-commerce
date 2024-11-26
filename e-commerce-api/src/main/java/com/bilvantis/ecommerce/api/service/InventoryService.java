package com.bilvantis.ecommerce.api.service;

import com.bilvantis.ecommerce.dao.data.model.Order;
import com.bilvantis.ecommerce.dto.model.InventoryDTO;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface InventoryService<I extends InventoryDTO, ID extends Serializable> {
    I updateInventory(String productId, Integer quantity);

    I getInventoryByProductId(String productId);

    List<I> getAllInventories();
    void monitorLowStockItems();
    Boolean isStockAvailable(Map<String, Integer> items);
    Boolean reserveStock(Map<String, Integer> items);
    Boolean rollbackStock(Order order);
}
