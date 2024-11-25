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

import static com.bilvantis.ecommerce.api.util.CategorySupport.*;

@Service("categoryServiceImpl")
public class CategoryServiceImpl implements CategoryService<CategoryDTO, UUID> {


    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

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

    @Override
    @Transactional
    public CategoryDTO updateCategory(String categoryId, CategoryDTO categoryDTO) {
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        updateCategoryFromDTO(existingCategory, categoryDTO);
        categoryRepository.save(existingCategory);
        return convertCategoryEntityToCategoryDTO(existingCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        categoryRepository.delete(category);
    }

    @Override
    public CategoryDTO getCategoryById(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        return convertCategoryEntityToCategoryDTO(category);
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategorySupport::convertCategoryEntityToCategoryDTO)
                .collect(Collectors.toList());
    }
}
