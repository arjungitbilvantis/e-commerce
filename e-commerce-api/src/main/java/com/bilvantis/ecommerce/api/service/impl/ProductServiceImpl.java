package com.bilvantis.ecommerce.api.service.impl;

import com.bilvantis.ecommerce.api.exception.ApplicationException;
import com.bilvantis.ecommerce.api.service.ProductService;
import com.bilvantis.ecommerce.api.util.ProductSupport;
import com.bilvantis.ecommerce.dao.data.model.Category;
import com.bilvantis.ecommerce.dao.data.model.Inventory;
import com.bilvantis.ecommerce.dao.data.model.Product;
import com.bilvantis.ecommerce.dao.data.repository.CategoryRepository;
import com.bilvantis.ecommerce.dao.data.repository.InventoryRepository;
import com.bilvantis.ecommerce.dao.data.repository.ProductRepository;
import com.bilvantis.ecommerce.dto.model.CategoryDTO;
import com.bilvantis.ecommerce.dto.model.ProductDTO;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.bilvantis.ecommerce.api.util.ProductConstants.*;
import static com.bilvantis.ecommerce.api.util.ProductSupport.*;

@Service("productServiceImpl")
public class ProductServiceImpl implements ProductService<ProductDTO, UUID> {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final InventoryRepository inventoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, InventoryRepository inventoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.inventoryRepository = inventoryRepository;
    }

    /**
     * Creates a new product based on the provided ProductDTO.
     *
     * @param productDTO the ProductDTO object containing the product data
     * @return the created ProductDTO object
     * @throws ApplicationException if there is an error during the save operation
     */
    @Transactional
    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        try {
            // Create a new product entity from the DTO
            Product product = convertProductDTOToProductEntity(productDTO);

            // Generate a random UUID for productId before saving
            UUID generatedProductId = UUID.randomUUID();
            product.setProductId(generatedProductId.toString());

            // Validate and set category
            setCategory(product, productDTO);

            // Validate and set sub-category
            setSubCategory(product, productDTO);

            // Save the product entity
            Product savedProduct = productRepository.save(product);

            // Create and save the inventory entity with available items
            createAndSaveInventory(savedProduct, productDTO.getAvailableItems());

            // Convert and return the saved product entity to DTO
            return convertProductEntityToProductDTO(savedProduct);
        } catch (DataAccessException e) {
            throw new ApplicationException(PRODUCT_SAVE_FAILED);
        }
    }

    /**
     * Retrieves and updates the category based on the provided CategoryDTO.
     *
     * @param categoryDTO the data transfer object containing category details
     * @return the updated Category entity
     * @throws ApplicationException if the category ID is missing or the category is not found
     */
    private Category getCategory(CategoryDTO categoryDTO) {
        if (Objects.nonNull(categoryDTO) && Objects.nonNull(categoryDTO.getCategoryId())) {
            Optional<Category> existingCategory = categoryRepository.findById(categoryDTO.getCategoryId());

            if (existingCategory.isPresent()) {
                // Update the existing category with the values from the DTO
                Category category = existingCategory.get();
                category.setCategoryName(categoryDTO.getCategoryName());
                return category;
            } else {
                throw new ApplicationException(String.format(CATEGORY_NOT_FOUND, categoryDTO.getCategoryId()));
            }
        } else {
            throw new ApplicationException(CATEGORY_ID_MISSING);
        }
    }

    /**
     * Sets the category for the given product based on the provided ProductDTO.
     *
     * @param product    the product entity to set the category for
     * @param productDTO the data transfer object containing product details
     * @throws ApplicationException if the category ID is missing or the category is not found
     */
    private void setCategory(Product product, ProductDTO productDTO) {
        product.setCategory(getCategory(productDTO.getCategory()));
    }

    /**
     * Sets the sub-category for the given product based on the provided ProductDTO.
     *
     * @param product    the product entity to set the sub-category for
     * @param productDTO the data transfer object containing product details
     * @throws ApplicationException if the sub-category ID is missing or the sub-category is not found
     */
    private void setSubCategory(Product product, ProductDTO productDTO) {
        product.setSubCategory(getCategory(productDTO.getSubCategory()));
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param productId the ID of the product to retrieve
     * @return the ProductDTO object representing the retrieved product
     * @throws ApplicationException if the product is not found or if there is an error during the fetch operation
     */
    @Override
    public ProductDTO getProductByProductId(String productId) {
        try {
            // Look up the product by productId
            Optional<Product> productOptional = productRepository.findById(productId);

            if (productOptional.isPresent()) {
                // Convert entity to DTO and return it
                return convertProductEntityToProductDTO(productOptional.get());
            } else {
                throw new ApplicationException(String.format(PRODUCT_NOT_FOUND, productId));
            }
        } catch (DataAccessException e) {
            throw new ApplicationException(PRODUCT_FETCH_FAILED);
        }
    }

    /**
     * Retrieves all products.
     *
     * @return a list of ProductDTO objects representing all products
     * @throws ApplicationException if no products are found or if there is an error during the fetch operation
     */
    @Override
    public List<ProductDTO> getAllProducts() {
        try {
            // Fetch all products from the repository
            List<Product> products = productRepository.findAll();

            // Check if the list is empty and throw an exception if no products are found
            if (products.isEmpty()) {
                throw new ApplicationException(NO_PRODUCTS_FOUND);
            }

            // Convert the list of entities to a list of DTOs
            return convertProductEntityToProductDTOList(products);
        } catch (DataAccessException e) {
            throw new ApplicationException(PRODUCTS_FETCH_FAILED);
        }
    }

    /**
     * Updates an existing product by its ID.
     *
     * @param productId  the ID of the product to be updated
     * @param productDTO the ProductDTO object containing the updated product details
     * @return the updated ProductDTO object
     * @throws ApplicationException if the product is not found, if there is a mismatch in product IDs,
     *                              or if there is an error during the update operation
     */
    @Transactional
    @Override
    public ProductDTO updateProduct(String productId, ProductDTO productDTO) {
        try {
            // Validate that the provided productId matches the productDTO's productId
            if (!Objects.equals(productId, productDTO.getProductId())) {
                throw new ApplicationException(String.format(PRODUCT_ID_MISMATCH, productId, productDTO.getProductId()));
            }

            // Retrieve the existing product by productId
            Optional<Product> existingProductOptional = productRepository.findById(productId);

            if (existingProductOptional.isEmpty()) {
                throw new ApplicationException(String.format(PRODUCT_NOT_FOUND, productId));
            }

            Product existingProduct = existingProductOptional.get();

            // Validate and update the category
            if (Objects.nonNull(productDTO.getCategory()) && Objects.nonNull(productDTO.getCategory().getCategoryId())) {
                Optional<Category> existingCategory = categoryRepository.findById(productDTO.getCategory().getCategoryId());

                if (existingCategory.isPresent()) {
                    Category category = existingCategory.get();
                    category.setCategoryName(productDTO.getCategory().getCategoryName());
                    existingProduct.setCategory(category);
                } else {
                    throw new ApplicationException(String.format(CATEGORY_NOT_FOUND, productDTO.getCategory().getCategoryId())
                    );
                }
            } else {
                throw new ApplicationException(CATEGORY_ID_MISSING);
            }

            // Validate and update the sub-category
            if (Objects.nonNull(productDTO.getSubCategory()) && Objects.nonNull(productDTO.getSubCategory().getCategoryId())) {
                Optional<Category> existingSubCategory = categoryRepository.findById(productDTO.getSubCategory().getCategoryId());

                if (existingSubCategory.isPresent()) {
                    Category subCategory = existingSubCategory.get();
                    subCategory.setCategoryName(productDTO.getSubCategory().getCategoryName());
                    existingProduct.setSubCategory(subCategory);
                } else {
                    throw new ApplicationException(
                            String.format(SUBCATEGORY_NOT_FOUND, productDTO.getSubCategory().getCategoryId())
                    );
                }
            } else {
                throw new ApplicationException(SUBCATEGORY_ID_MISSING);
            }

            // Update the remaining fields of the product
            updateProductFromDTO(existingProduct, productDTO);

            // Save the updated product entity
            Product updatedProduct = productRepository.save(existingProduct);

            // Update inventory associated with the product
            updateInventory(updatedProduct, productDTO);

            // Convert and return the updated product entity to DTO
            return convertProductEntityToProductDTO(updatedProduct);

        } catch (DataAccessException e) {
            throw new ApplicationException(String.format(ERROR_UPDATING_PRODUCT, productId));
        }
    }


    /**
     * Soft Deletes a product by its ID.
     *
     * @param productId the ID of the product to be deleted
     * @throws ApplicationException if the product is not found or if there is an error during the delete operation
     */
    @Transactional
    @Override
    public void deleteProduct(String productId) {
        try {
            // Check if the product exists in the repository
            Optional<Product> productOptional = productRepository.findById(productId);

            if (productOptional.isEmpty()) {
                throw new ApplicationException(String.format(PRODUCT_NOT_FOUND, productId));
            }

            // Retrieve the product entity
            Product product = productOptional.get();

            // Mark the product as inactive
            product.setIsActive(Boolean.FALSE);
            productRepository.save(product);

            // Check if there is any inventory associated with the product
            Optional<Inventory> inventoryOptional = inventoryRepository.findByProductId(productId);

            // Mark the inventory as inactive
            if (inventoryOptional.isPresent()) {
                Inventory inventory = inventoryOptional.get();
                inventory.setIsActive(Boolean.FALSE);
                inventoryRepository.save(inventory);
            }
        } catch (DataAccessException e) {
            throw new ApplicationException(String.format(ERROR_DELETING_PRODUCT, productId));
        }
    }


    /**
     * Converts a list of Product entities to a list of ProductDTO objects.
     *
     * @param products the list of Product entities to be converted
     * @return a list of ProductDTO objects
     */
    private List<ProductDTO> convertProductEntityToProductDTOList(List<Product> products) {
        return products.stream()
                .map(ProductSupport::convertProductEntityToProductDTO)
                .toList();
    }

    /**
     * Creates and saves an inventory record for the given product.
     *
     * @param savedProduct the product for which the inventory record is to be created
     */
    private void createAndSaveInventory(Product savedProduct, Integer availableItems) {
        Inventory inventory = new Inventory();
        inventory.setInventoryId(UUID.randomUUID().toString());
        inventory.setProductId(savedProduct.getProductId());

        // Use provided available items if available, otherwise set a default value
        inventory.setAvailableItems(Objects.nonNull(availableItems) ? availableItems : 10);

        // Set the default lower threshold
        inventory.setLowerThreshold(5);
        inventory.setIsActive(Boolean.TRUE);

        inventoryRepository.save(inventory);
    }


    /**
     * Updates the inventory for the given product. If the inventory exists, it updates the details.
     * If no inventory exists, it creates a new inventory record.
     *
     * @param updatedProduct the product for which the inventory is to be updated
     */
    private void updateInventory(Product updatedProduct, ProductDTO productDTO) {
        Optional<Inventory> inventoryOptional = inventoryRepository.findByProductId(updatedProduct.getProductId());

        if (inventoryOptional.isPresent()) {
            Inventory inventory = inventoryOptional.get();

            // Adjust available items if the value is provided in DTO
            if (Objects.nonNull(productDTO.getAvailableItems())) {
                int updatedAvailableItems = inventory.getAvailableItems() + productDTO.getAvailableItems();
                inventory.setAvailableItems(updatedAvailableItems);
            }

            // Save the updated inventory
            inventoryRepository.save(inventory);
        } else {
            throw new ApplicationException(String.format(INVENTORY_NOT_FOUND, updatedProduct.getProductId()));
        }
    }
    
}

