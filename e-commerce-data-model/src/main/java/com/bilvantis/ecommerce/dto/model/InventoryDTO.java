package com.bilvantis.ecommerce.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDTO extends BaseDTO implements Serializable {

    private String inventoryId;
    private String productId;
    private Integer availableItems;
    private Integer lowerThreshold;
}
