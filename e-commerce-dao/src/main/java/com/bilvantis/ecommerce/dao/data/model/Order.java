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
@Table(name = "`order`")
public class Order extends BaseEntity implements Serializable {

    @Id
    @Column(name = "order_id", columnDefinition = "VARCHAR(36)")
    private String orderId;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "status")
    private String status;
    @Column(name = "payment_status")
    private String paymentStatus;
}
