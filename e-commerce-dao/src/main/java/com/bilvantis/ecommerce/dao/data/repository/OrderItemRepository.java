package com.bilvantis.ecommerce.dao.data.repository;

import com.bilvantis.ecommerce.dao.data.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
