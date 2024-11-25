package com.bilvantis.ecommerce.controller;


import com.bilvantis.ecommerce.api.service.CategoryService;
import com.bilvantis.ecommerce.dto.model.CategoryDTO;
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
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService<CategoryDTO, UUID> categoryService;

    public CategoryController(CategoryService<CategoryDTO, UUID> categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Creates a new category based on the provided CategoryDTO.
     *
     * @param categoryDTO the CategoryDTO object containing the category data
     * @return a ResponseEntity containing the created CategoryDTO and HTTP status code 201 (Created)
     */
    @PostMapping
    public ResponseEntity<UserResponseDTO> createCategory(@NotNull @Valid @RequestBody CategoryDTO categoryDTO) {
        return new ResponseEntity<>(UserRequestResponseBuilder.buildResponseDTO(
                categoryService.createCategory(categoryDTO), null, ECommerceAppConstant.SUCCESS), HttpStatus.CREATED);
    }

    /**
     * Retrieves a category by its ID.
     *
     * @param id the UUID of the category to retrieve
     * @return a ResponseEntity containing the retrieved CategoryDTO and HTTP status code 200 (OK)
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getCategoryById(@NotNull @PathVariable UUID id) {
        CategoryDTO categoryDTO = categoryService.getCategoryById(id.toString());
        return new ResponseEntity<>(UserRequestResponseBuilder.buildResponseDTO(
                categoryDTO, null, ECommerceAppConstant.SUCCESS), HttpStatus.OK);
    }

    /**
     * Retrieves all categories.
     *
     * @return a ResponseEntity containing a list of all CategoryDTOs and HTTP status code 200 (OK)
     */
    @GetMapping
    public ResponseEntity<UserResponseDTO> getAllCategories() {
        List<CategoryDTO> categoryDTOList = categoryService.getAllCategories();
        return new ResponseEntity<>(UserRequestResponseBuilder.buildResponseDTO(
                categoryDTOList, null, ECommerceAppConstant.SUCCESS), HttpStatus.OK);
    }

    /**
     * Updates an existing category by its ID.
     *
     * @param id          the ID of the category to be updated
     * @param categoryDTO the CategoryDTO object containing the updated category details
     * @return a ResponseEntity containing the updated CategoryDTO and HTTP status code 200 (OK)
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateCategory(@NotNull @PathVariable String id,
                                                          @NotNull @Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO updatedCategoryDTO = categoryService.updateCategory(id, categoryDTO);
        return new ResponseEntity<>(UserRequestResponseBuilder.buildResponseDTO(
                updatedCategoryDTO, null, ECommerceAppConstant.SUCCESS), HttpStatus.OK);
    }

    /**
     * Deletes a category by its ID.
     *
     * @param id the ID of the category to be deleted
     * @return a ResponseEntity with HTTP status code 204 (No Content)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponseDTO> deleteCategory(@NotNull @PathVariable String id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(UserRequestResponseBuilder.buildResponseDTO(
                null, null, ECommerceAppConstant.SUCCESS), HttpStatus.NO_CONTENT);
    }
}
