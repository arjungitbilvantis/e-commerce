package com.bilvantis.ecommerce.controller;

import com.bilvantis.ecommerce.api.service.ProductService;
import com.bilvantis.ecommerce.dto.model.ProductDTO;
import com.bilvantis.ecommerce.dto.util.OnCreate;
import com.bilvantis.ecommerce.dto.util.OnUpdate;
import com.bilvantis.ecommerce.model.UserResponseDTO;
import com.bilvantis.ecommerce.util.ECommerceAppConstant;
import com.bilvantis.ecommerce.util.UserRequestResponseBuilder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService<ProductDTO, UUID> productService;

    public ProductController(ProductService<ProductDTO, UUID> productService) {
        this.productService = productService;
    }

    /**
     * Creates a new product based on the provided ProductDTO.
     *
     * @param productDTO the ProductDTO object containing the product data
     * @return a ResponseEntity containing the created ProductDTO and HTTP status code 201 (Created)
     */
    @Validated(OnCreate.class)
    @PostMapping
    public ResponseEntity<UserResponseDTO> createProduct(@NotNull @Valid @RequestBody ProductDTO productDTO) {
        return new ResponseEntity<>(UserRequestResponseBuilder.buildResponseDTO(
                productService.createProduct(productDTO), null, ECommerceAppConstant.SUCCESS), HttpStatus.CREATED);
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param id the UUID of the product to retrieve
     * @return a ResponseEntity containing the retrieved ProductDTO and HTTP status code 200 (OK)
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getProductByProductId(@NotNull @PathVariable UUID id) {
        ProductDTO productDTO = productService.getProductByProductId(id.toString());
        return new ResponseEntity<>(UserRequestResponseBuilder.buildResponseDTO(
                productDTO, null, ECommerceAppConstant.SUCCESS), HttpStatus.OK);
    }

    /**
     * Retrieves all products.
     *
     * @return a ResponseEntity containing a list of all ProductDTOs and HTTP status code 200 (OK)
     */
    @GetMapping
    public ResponseEntity<UserResponseDTO> getAllProducts() {
        List<ProductDTO> productDTOList = productService.getAllProducts();
        return new ResponseEntity<>(UserRequestResponseBuilder.buildResponseDTO(
                productDTOList, null, ECommerceAppConstant.SUCCESS), HttpStatus.OK);
    }

    /**
     * Updates an existing product by its ID.
     *
     * @param id         the ID of the product to be updated
     * @param productDTO the ProductDTO object containing the updated product details
     * @return a ResponseEntity containing the updated ProductDTO and HTTP status code 200 (OK)
     */
    @Validated(OnUpdate.class)
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateProduct(@NotNull @PathVariable String id,
                                                         @NotNull @Valid @RequestBody ProductDTO productDTO) {
        ProductDTO updatedProductDTO = productService.updateProduct(id, productDTO);
        return new ResponseEntity<>(UserRequestResponseBuilder.buildResponseDTO(
                updatedProductDTO, null, ECommerceAppConstant.SUCCESS), HttpStatus.OK);
    }

    /**
     * Deletes a product by its ID.
     *
     * @param id the ID of the product to be deleted
     * @return a ResponseEntity with HTTP status code 204 (No Content)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponseDTO> deleteProduct(@NotNull @PathVariable String id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(UserRequestResponseBuilder.buildResponseDTO(
                null, null, ECommerceAppConstant.SUCCESS), HttpStatus.NO_CONTENT);
    }
}
