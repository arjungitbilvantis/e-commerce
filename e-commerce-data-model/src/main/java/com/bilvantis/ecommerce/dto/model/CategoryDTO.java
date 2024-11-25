package com.bilvantis.ecommerce.dto.model;

import com.bilvantis.ecommerce.dto.util.OnCreate;
import com.bilvantis.ecommerce.dto.util.OnUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
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

    @Null(groups = OnCreate.class, message = MESSAGE_ID_ON_CREATE)
    @NotBlank(groups = OnUpdate.class, message = MESSAGE_ID_ON_UPDATE)
    private String categoryId;

    @NotBlank(message = CATEGORY_NAME_REQUIRED_MESSAGE)
    @Size(max = NAME_MAX_LENGTH, message = CATEGORY_NAME_LENGTH_MESSAGE)
    private String categoryName;

    private String parentCategoryId;

}
