package com.bilvantis.ecommerce.dao.data.repository;

import com.bilvantis.ecommerce.dao.data.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,String> {
    List<Product> findByCategory_CategoryId(String categoryId);
}
