package com.bilvantis.ecommerce.dto.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO extends BaseDTO implements Serializable {
    private String categoryId;

    @NotBlank(message = "Category name is required.")
    @Size(max = 150, message = "Category name must not exceed 150 characters.")
    private String categoryName;

    private String parentCategoryId;

}
