package com.bilvantis.ecommerce.api.service;

import com.bilvantis.ecommerce.dto.model.ProductDTO;

import java.io.Serializable;
import java.util.List;

public interface ProductService<I extends ProductDTO, ID extends Serializable> {

    I createProduct(I product);

    I getProductByProductId(String productId);

    List<I> getAllProducts();

    I updateProduct(String productId, I productDetails);

    void deleteProduct(String productId);

}
