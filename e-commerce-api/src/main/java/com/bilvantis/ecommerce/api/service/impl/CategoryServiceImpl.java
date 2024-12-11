package com.bilvantis.ecommerce.api.service.impl;

import com.bilvantis.ecommerce.api.exception.ResourceNotFoundException;
import com.bilvantis.ecommerce.api.service.CategoryService;
import com.bilvantis.ecommerce.api.util.CategorySupport;
import com.bilvantis.ecommerce.dao.data.model.Category;
import com.bilvantis.ecommerce.dao.data.repository.CategoryRepository;
import com.bilvantis.ecommerce.dto.model.CategoryDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.bilvantis.ecommerce.api.util.CategoryConstants.CATEGORY_NOT_FOUND;
import static com.bilvantis.ecommerce.api.util.CategorySupport.*;

@Service("categoryServiceImpl")
public class CategoryServiceImpl implements CategoryService<CategoryDTO, UUID> {


    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Creates a new category.
     *
     * @param categoryDTO the category data transfer object containing the details of the category to be created
     * @return the created category as a data transfer object
     */
    @Override
    @Transactional
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = convertCategoryDTOToCategoryEntity(categoryDTO);

        // Generate a random UUID for categoryId before saving
        UUID generatedCategoryId = UUID.randomUUID();
        category.setCategoryId(generatedCategoryId.toString());

        categoryRepository.save(category);
        return convertCategoryEntityToCategoryDTO(category);
    }

    /**
     * Updates an existing category.
     *
     * @param categoryId  the ID of the category to be updated
     * @param categoryDTO the category data transfer object containing the updated details of the category
     * @return the updated category as a data transfer object
     * @throws ResourceNotFoundException if the category with the specified ID is not found
     */
    @Override
    @Transactional
    public CategoryDTO updateCategory(String categoryId, CategoryDTO categoryDTO) {
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(CATEGORY_NOT_FOUND));
        updateCategoryFromDTO(existingCategory, categoryDTO);
        categoryRepository.save(existingCategory);
        return convertCategoryEntityToCategoryDTO(existingCategory);
    }

    /**
     * Deletes a category.
     *
     * @param categoryId the ID of the category to be deleted
     * @throws ResourceNotFoundException if the category with the specified ID is not found
     */
    @Override
    @Transactional
    public void deleteCategory(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(CATEGORY_NOT_FOUND));
        categoryRepository.delete(category);
    }

    /**
     * Retrieves a category by its ID.
     *
     * @param categoryId the ID of the category to be retrieved
     * @return the category as a data transfer object
     * @throws ResourceNotFoundException if the category with the specified ID is not found
     */
    @Override
    public CategoryDTO getCategoryById(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(CATEGORY_NOT_FOUND));
        return convertCategoryEntityToCategoryDTO(category);
    }

    /**
     * Retrieves all categories.
     *
     * @return a list of all categories as data transfer objects
     */
    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategorySupport::convertCategoryEntityToCategoryDTO)
                .collect(Collectors.toList());
    }
}
