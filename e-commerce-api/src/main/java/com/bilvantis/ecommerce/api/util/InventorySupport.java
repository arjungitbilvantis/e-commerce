package com.bilvantis.ecommerce.api.util;

import com.bilvantis.ecommerce.dao.data.model.Inventory;
import com.bilvantis.ecommerce.dto.model.InventoryDTO;

import java.util.Objects;

public class InventorySupport {

    /**
     * Converts an Inventory entity to an InventoryDTO object.
     *
     * @param inventory the Inventory entity containing the inventory data
     * @return an InventoryDTO object populated with the data from the Inventory entity
     */
    public static InventoryDTO convertInventoryEntityToInventoryDTO(Inventory inventory) {
        InventoryDTO inventoryDTO = new InventoryDTO();
        inventoryDTO.setInventoryId(inventory.getInventoryId());
        inventoryDTO.setProductId(inventory.getProductId());
        inventoryDTO.setAvailableItems(inventory.getAvailableItems());
        inventoryDTO.setLowerThreshold(inventory.getLowerThreshold());
        inventoryDTO.setCreatedBy(inventory.getCreatedBy());
        inventoryDTO.setCreatedDate(Objects.nonNull(inventory.getCreatedDate()) ? inventory.getCreatedDate() : null);
        inventoryDTO.setUpdatedBy(inventory.getUpdatedBy());
        inventoryDTO.setUpdatedDate(Objects.nonNull(inventory.getUpdatedDate()) ? inventory.getUpdatedDate() : null);
        return inventoryDTO;
    }

    /**
     * Converts an InventoryDTO object to an Inventory entity.
     *
     * @param inventoryDTO the InventoryDTO object containing the inventory data
     * @return an Inventory entity populated with the data from the InventoryDTO
     */
    public static Inventory convertInventoryDTOToInventoryEntity(InventoryDTO inventoryDTO) {
        Inventory inventory = new Inventory();
        inventory.setInventoryId(inventoryDTO.getInventoryId());
        inventory.setProductId(inventoryDTO.getProductId());
        inventory.setAvailableItems(inventoryDTO.getAvailableItems());
        inventory.setLowerThreshold(inventoryDTO.getLowerThreshold());
        inventory.setCreatedBy(inventoryDTO.getCreatedBy());
        inventory.setCreatedDate(Objects.nonNull(inventoryDTO.getCreatedDate()) ? inventoryDTO.getCreatedDate() : null);
        inventory.setUpdatedBy(inventoryDTO.getUpdatedBy());
        inventory.setUpdatedDate(Objects.nonNull(inventoryDTO.getUpdatedDate()) ? inventoryDTO.getUpdatedDate() : null);
        return inventory;
    }

    /**
     * Updates the fields of an existing Inventory object based on the values from an InventoryDTO object.
     *
     * @param existingInventory the Inventory object to be updated
     * @param inventoryDTO      the InventoryDTO object containing the new values
     */
    public static void updateInventoryFromDTO(Inventory existingInventory, InventoryDTO inventoryDTO) {
        existingInventory.setAvailableItems(inventoryDTO.getAvailableItems());
        existingInventory.setLowerThreshold(inventoryDTO.getLowerThreshold());
        existingInventory.setUpdatedBy(inventoryDTO.getUpdatedBy());
        existingInventory.setUpdatedDate(inventoryDTO.getUpdatedDate());
    }
}
