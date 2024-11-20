package com.bilvantis.ecommerce.api.util;

import com.bilvantis.ecommerce.dao.data.model.Category;
import com.bilvantis.ecommerce.dto.model.CategoryDTO;

import java.util.Date;
import java.util.Objects;

public class CategorySupport {

    /**
     * Converts a Category entity to a CategoryDTO object.
     *
     * @param category the Category entity containing the category data
     * @return a CategoryDTO object populated with the data from the Category entity
     */
    public static CategoryDTO convertCategoryEntityToCategoryDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryId(category.getCategoryId());
        categoryDTO.setCategoryName(category.getCategoryName());
        if (Objects.nonNull(category.getParentCategory())) {
            categoryDTO.setParentCategoryId(category.getParentCategory().getCategoryId());
        }
        categoryDTO.setIsActive(category.getIsActive());
        categoryDTO.setCreatedBy(category.getCreatedBy());
        categoryDTO.setCreatedDate(Objects.nonNull(category.getCreatedDate()) ? category.getCreatedDate() : null);
        categoryDTO.setUpdatedBy(category.getUpdatedBy());
        categoryDTO.setUpdatedDate(Objects.nonNull(category.getUpdatedDate()) ? category.getUpdatedDate() : null);
        return categoryDTO;
    }

    /**
     * Converts a CategoryDTO object to a Category entity.
     *
     * @param categoryDTO the CategoryDTO object containing the category data
     * @return a Category entity populated with the data from the CategoryDTO
     */
    public static Category convertCategoryDTOToCategoryEntity(CategoryDTO categoryDTO) {
        Category category = new Category();

        // Set category ID and name
        category.setCategoryId(categoryDTO.getCategoryId());
        category.setCategoryName(categoryDTO.getCategoryName());

        // Set parent category if parentCategoryId is provided
        if (Objects.nonNull(categoryDTO.getParentCategoryId())) {
            Category parentCategory = new Category();
            parentCategory.setCategoryId(categoryDTO.getParentCategoryId());
            category.setParentCategory(parentCategory);
        }
        category.setIsActive(Boolean.TRUE);
        category.setCreatedBy(categoryDTO.getCreatedBy());
        category.setCreatedDate(categoryDTO.getCreatedDate());
        category.setUpdatedBy(categoryDTO.getUpdatedBy());
        category.setUpdatedDate(categoryDTO.getUpdatedDate());

        return category;
    }

    /**
     * Updates the fields of an existing Category object based on the values from a CategoryDTO object.
     *
     * @param existingCategory the Category object to be updated
     * @param categoryDTO      the CategoryDTO object containing the new values
     */
    public static void updateCategoryFromDTO(Category existingCategory, CategoryDTO categoryDTO) {
        // Update category name
        existingCategory.setCategoryName(categoryDTO.getCategoryName());

        // Update parent category if parentCategoryId is provided
        if (Objects.nonNull(categoryDTO.getParentCategoryId())) {
            Category parentCategory = new Category();
            parentCategory.setCategoryId(categoryDTO.getParentCategoryId());
            existingCategory.setParentCategory(parentCategory);
        } else {
            existingCategory.setParentCategory(null); // Clear parent category if null
        }

        // Update audit fields
        existingCategory.setUpdatedBy(categoryDTO.getUpdatedBy());
        existingCategory.setUpdatedDate(new Date());
    }

}
