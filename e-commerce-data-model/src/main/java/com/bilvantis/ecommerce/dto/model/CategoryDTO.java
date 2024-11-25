package com.bilvantis.ecommerce.dto.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static com.bilvantis.ecommerce.dto.util.ECommerceDataModelConstants.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO extends BaseDTO implements Serializable {
    private String categoryId;

    @NotBlank(message = CATEGORY_NAME_REQUIRED_MESSAGE)
    @Size(max = NAME_MAX_LENGTH, message = CATEGORY_NAME_LENGTH_MESSAGE)
    private String categoryName;

    private String parentCategoryId;

}
