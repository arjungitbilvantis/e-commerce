package com.bilvantis.ecommerce.api.util;

import com.bilvantis.ecommerce.dao.data.model.Product;
import com.bilvantis.ecommerce.dto.model.ProductDTO;

import java.util.Objects;

import static com.bilvantis.ecommerce.api.util.CategorySupport.convertCategoryDTOToCategoryEntity;
import static com.bilvantis.ecommerce.api.util.CategorySupport.convertCategoryEntityToCategoryDTO;

public class ProductSupport {
    /**
     * Converts a Product entity to a ProductDTO object.
     *
     * @param product the Product entity containing the product data
     * @return a ProductDTO object populated with the data from the Product entity
     */
    public static ProductDTO convertProductEntityToProductDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(product.getProductId());
        productDTO.setProductName(product.getProductName());
        productDTO.setProductDescription(product.getProductDescription());
        productDTO.setImageUrl(product.getImageUrl());
        productDTO.setPrice(product.getPrice());
        // Convert Category entity to CategoryDTO
        if (Objects.nonNull(product.getCategory())) {
            productDTO.setCategory(convertCategoryEntityToCategoryDTO(product.getCategory()));
        }
        if (Objects.nonNull(product.getSubCategory())) {
            productDTO.setSubCategory(convertCategoryEntityToCategoryDTO(product.getSubCategory()));
        }
        productDTO.setIsActive(product.getIsActive());
        productDTO.setCreatedBy(product.getCreatedBy());
        productDTO.setCreatedDate(Objects.nonNull(product.getCreatedDate()) ? product.getCreatedDate() : null);
        productDTO.setUpdatedBy(product.getUpdatedBy());
        productDTO.setUpdatedDate(Objects.nonNull(product.getUpdatedDate()) ? product.getUpdatedDate() : null);
        return productDTO;
    }

    /**
     * Converts a ProductDTO object to a Product entity.
     *
     * @param productDTO the ProductDTO object containing the product data
     * @return a Product entity populated with the data from the ProductDTO
     */
    public static Product convertProductDTOToProductEntity(ProductDTO productDTO) {
        Product product = new Product();
        product.setProductId(productDTO.getProductId());
        product.setProductName(productDTO.getProductName());
        product.setProductDescription(productDTO.getProductDescription());
        product.setImageUrl(productDTO.getImageUrl());
        product.setPrice(productDTO.getPrice());
        // Convert CategoryDTO to Category entity
        if (Objects.nonNull(productDTO.getCategory())) {
            product.setCategory(convertCategoryDTOToCategoryEntity(productDTO.getCategory()));
        }
        if (Objects.nonNull(productDTO.getSubCategory())) {
            product.setSubCategory(convertCategoryDTOToCategoryEntity(productDTO.getSubCategory()));
        }
        product.setIsActive(Boolean.TRUE);
        product.setCreatedBy(productDTO.getCreatedBy());
        product.setCreatedDate(Objects.nonNull(productDTO.getCreatedDate()) ? productDTO.getCreatedDate() : null);
        product.setUpdatedBy(productDTO.getUpdatedBy());
        product.setUpdatedDate(Objects.nonNull(productDTO.getUpdatedDate()) ? productDTO.getUpdatedDate() : null);
        return product;
    }

    /**
     * Updates the fields of an existing Product object based on the values from a ProductDTO object.
     *
     * @param existingProduct the Product object to be updated
     * @param productDTO      the ProductDTO object containing the new values
     */
    public static void updateProductFromDTO(Product existingProduct, ProductDTO productDTO) {
        existingProduct.setProductName(productDTO.getProductName());
        existingProduct.setProductDescription(productDTO.getProductDescription());
        existingProduct.setImageUrl(productDTO.getImageUrl());
        existingProduct.setPrice(productDTO.getPrice());

        // Convert CategoryDTO to Category and set it to the existing product
        if (Objects.nonNull(productDTO.getCategory())) {
            existingProduct.setCategory(convertCategoryDTOToCategoryEntity(productDTO.getCategory()));
        }
        if (Objects.nonNull(productDTO.getSubCategory())) {
            existingProduct.setSubCategory(convertCategoryDTOToCategoryEntity(productDTO.getSubCategory()));
        }
        existingProduct.setUpdatedBy(productDTO.getUpdatedBy());
        existingProduct.setUpdatedDate(productDTO.getUpdatedDate());
    }
}
