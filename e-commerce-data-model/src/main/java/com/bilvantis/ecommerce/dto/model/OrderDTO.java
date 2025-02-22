package com.bilvantis.ecommerce.dto.model;

import com.bilvantis.ecommerce.dto.util.OnCreate;
import com.bilvantis.ecommerce.dto.util.OnUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

import static com.bilvantis.ecommerce.dto.util.ECommerceDataModelConstants.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO extends BaseDTO implements Serializable {
    @Null(groups = OnCreate.class, message = MESSAGE_ID_ON_CREATE)
    @NotBlank(groups = OnUpdate.class, message = MESSAGE_ID_ON_UPDATE)
    private String orderId;

    @NotBlank(message = MESSAGE_USER_ID)
    private String userId;

    @NotBlank(message = ORDER_STATUS_NOT_BLANK)
    private String status;

    @NotBlank(message = PAYMENT_STATUS_NOT_BLANK)
    private String paymentStatus;

    @NotNull(message = ORDER_ITEMS_NOT_NULL)
    @Size(min = ONE, message = ORDER_ITEMS_SIZE_MIN)
    private List<OrderItemDTO> items;
}
