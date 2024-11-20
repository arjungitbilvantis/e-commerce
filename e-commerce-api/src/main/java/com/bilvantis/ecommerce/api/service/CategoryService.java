package com.bilvantis.ecommerce.api.service;

import com.bilvantis.ecommerce.dto.model.CategoryDTO;

import java.io.Serializable;
import java.util.List;

public interface CategoryService<I extends CategoryDTO, ID extends Serializable> {
    I createCategory(I categoryDTO);

    I updateCategory(String categoryId, I categoryDTO);

    void deleteCategory(String categoryId);

    I getCategoryById(String categoryId);

    List<I> getAllCategories();
}
