package com.bilvantis.ecommerce.dto.model;

import com.bilvantis.ecommerce.dto.util.OnCreate;
import com.bilvantis.ecommerce.dto.util.OnUpdate;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

import static com.bilvantis.ecommerce.dto.util.ECommerceDataModelConstants.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO extends BaseDTO implements Serializable {

    @Null(groups = OnCreate.class, message = MESSAGE_ID_ON_CREATE)
    @NotNull(groups = OnUpdate.class, message = MESSAGE_ID_ON_UPDATE)
    private String productId;

    @NotBlank(message = MESSAGE_PRODUCT_NAME)
    @Size(max = NAME_MAX_LENGTH, message = NAME_LENGTH_MESSAGE)
    @Pattern(regexp = REGEX_PATTERN_FOR_NAMES, message = NAME_MESSAGE)
    private String productName;

    @Size(max = PRODUCT_DESCRIPTION, message = DESCRIPTION_PRODUCT_LENGTH_MESSAGE)
    private String productDescription;

    @Size(max = URL_LENGTH, message = URL_LENGTH_MESSAGE)
    private String imageUrl;

    @NotNull(message = MESSAGE_PRICE_REQUIRED)
    @DecimalMin(value = MIN_PRICE, inclusive = false, message = MESSAGE_PRICE_MIN)
    private BigDecimal price;

    @NotNull(message = MESSAGE_CATEGORY_REQUIRED)
    private CategoryDTO category;

    @NotNull(message = MESSAGE_SUB_CATEGORY_REQUIRED)
    private CategoryDTO subCategory;

    private Integer availableItems;
}
