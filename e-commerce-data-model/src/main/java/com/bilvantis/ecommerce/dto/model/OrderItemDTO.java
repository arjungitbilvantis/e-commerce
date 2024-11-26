package com.bilvantis.ecommerce.dto.model;

import com.bilvantis.ecommerce.dto.util.OnCreate;
import com.bilvantis.ecommerce.dto.util.OnUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static com.bilvantis.ecommerce.dto.util.ECommerceDataModelConstants.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO extends BaseDTO implements Serializable {

    @Null(groups = OnCreate.class, message = MESSAGE_ID_ON_CREATE)
    @NotBlank(groups = OnUpdate.class, message = MESSAGE_ID_ON_UPDATE)
    private String orderItemId;

    @NotBlank(message = MESSAGE_ORDER_ID)
    private String orderId;

    @NotBlank(message = MESSAGE_PRODUCT_ID)
    private String productId;

    private Integer quantity;

}
