package com.bilvantis.ecommerce.util;

import com.bilvantis.ecommerce.api.exception.ErrorResponse;
import com.bilvantis.ecommerce.model.UserResponseDTO;

import java.util.List;


public class UserRequestResponseBuilder {

    /**
     * Builds a {@link UserResponseDTO} with the provided body, status, and error details.
     * This is used to standardize the response format for API or service results.
     */
    public static UserResponseDTO buildResponseDTO(Object body, List<ErrorResponse> errors, String status) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setBody(body);
        userResponseDTO.setErrors(errors);
        userResponseDTO.setStatus(status);
        return userResponseDTO;
    }


}
