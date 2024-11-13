package com.bilvantis.ecommerce.app.services.model;

import com.bilvantis.ecommerce.api.exception.ErrorResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserResponseDTO {

    private Object body;
    private String status;
    private List<ErrorResponse> errors;

}
