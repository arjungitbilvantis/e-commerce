package com.bilvantis.ecommerce.dao.data.model;

import com.bilvantis.ecommerce.dao.util.BaseEntity;
import com.bilvantis.ecommerce.dao.util.CommonEntityListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(CommonEntityListener.class)
@Table(name = "order_items")
public class OrderItem extends BaseEntity implements Serializable {
    @Id
    @Column(name = "order_item_id", columnDefinition = "VARCHAR(36)")
    private String orderItemId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(name = "quantity", nullable = false)
    private int quantity;
}
