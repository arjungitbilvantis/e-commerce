package com.bilvantis.ecommerce.dao.data.model;

import com.bilvantis.ecommerce.dao.util.BaseEntity;
import com.bilvantis.ecommerce.dao.util.CommonEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(CommonEntityListener.class)
public class Inventory extends BaseEntity implements Serializable {

    @Id
    @Column(name = "inventory_id", columnDefinition = "VARCHAR(36)")
    private String inventoryId;

    @Column(name = "product_id")
    private String productId;

    @Column(name = "available_items")
    private Integer availableItems;

    @Column(name = "lower_threshold")
    private Integer lowerThreshold;

    @Column(name = "reserved_items")
    private Integer reservedItems;
}
