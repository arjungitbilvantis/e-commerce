package com.bilvantis.ecommerce.app.services.controller.exception;

import com.bilvantis.ecommerce.api.exception.ApplicationException;
import com.bilvantis.ecommerce.api.exception.ErrorResponse;
import com.bilvantis.ecommerce.app.services.model.UserResponseDTO;
import com.bilvantis.ecommerce.app.services.util.UserRequestResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

import static com.bilvantis.ecommerce.app.services.util.ECommerceAppConstant.*;

@ControllerAdvice
public class ECommerceAppExceptionHandler {

    /**
     * Handle ApplicationExceptions
     *
     * @param exception ApplicationException
     * @return ErrorResponse
     */
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<UserResponseDTO> handleApplicationException(ApplicationException exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), GLOBAL_FIELD_ID);
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        List<ErrorResponse> errors = new ArrayList<>();
        errors.add(errorResponse);
        return new ResponseEntity<>(UserRequestResponseBuilder.buildResponseDTO(null, errors, ERROR), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles MethodArgumentNotValidException and returns a response with validation error details.
     *
     * @param exception the MethodArgumentNotValidException thrown when validation fails
     * @return a ResponseEntity containing a UserResponseDTO with error details and a BAD_REQUEST status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<UserResponseDTO> handleDepartmentServiceMethodArgumentException(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();
        List<ErrorResponse> errors = new ArrayList<>();
        bindingResult.getFieldErrors().forEach(fieldError ->
                errors.add(new ErrorResponse(fieldError.getDefaultMessage(), DEP_BADREQUEST_001))
        );
        return new ResponseEntity<>(UserRequestResponseBuilder.buildResponseDTO(null, errors, ERROR), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles HttpMessageNotReadableException and returns a response with error details.
     *
     * @param exception the HttpMessageNotReadableException thrown when a message is not readable
     * @return a ResponseEntity containing a UserResponseDTO with error details and a BAD_REQUEST status
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<UserResponseDTO> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        String message = exception.getMessage();
        if (message.contains(USER_TYPE_DTO)) {
            message = String.format(INVALID_ENUM_VALUE, USER_TYPE_DTO, ACCEPTED_USER_TYPE_VALUES);
        }
        ErrorResponse errorResponse = new ErrorResponse(message, GLOBAL_FIELD_ID);
        List<ErrorResponse> errors = new ArrayList<>();
        errors.add(errorResponse);
        return new ResponseEntity<>(UserRequestResponseBuilder.buildResponseDTO(null, errors, ERROR), HttpStatus.BAD_REQUEST);
    }

}
