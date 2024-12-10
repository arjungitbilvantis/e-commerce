package com.bilvantis.ecommerce.controller.exception;

import com.bilvantis.ecommerce.api.exception.ApplicationException;
import com.bilvantis.ecommerce.api.exception.ErrorResponse;
import com.bilvantis.ecommerce.model.UserResponseDTO;
import com.bilvantis.ecommerce.util.UserRequestResponseBuilder;
import com.bilvantis.ecommerce.util.ECommerceAppConstant;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.StreamSupport;

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
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), ECommerceAppConstant.GLOBAL_FIELD_ID);
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        List<ErrorResponse> errors = new ArrayList<>();
        errors.add(errorResponse);
        return new ResponseEntity<>(UserRequestResponseBuilder.buildResponseDTO(null, errors, ECommerceAppConstant.ERROR), HttpStatus.BAD_REQUEST);
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
                errors.add(new ErrorResponse(fieldError.getDefaultMessage(), ECommerceAppConstant.DEP_BADREQUEST_001))
        );
        return new ResponseEntity<>(UserRequestResponseBuilder.buildResponseDTO(null, errors, ECommerceAppConstant.ERROR), HttpStatus.BAD_REQUEST);
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
        if (message.contains(ECommerceAppConstant.USER_TYPE_DTO)) {
            message = String.format(ECommerceAppConstant.INVALID_ENUM_VALUE, ECommerceAppConstant.USER_TYPE_DTO, ECommerceAppConstant.ACCEPTED_USER_TYPE_VALUES);
        }
        ErrorResponse errorResponse = new ErrorResponse(message, ECommerceAppConstant.GLOBAL_FIELD_ID);
        List<ErrorResponse> errors = new ArrayList<>();
        errors.add(errorResponse);
        return new ResponseEntity<>(UserRequestResponseBuilder.buildResponseDTO(null, errors, ECommerceAppConstant.ERROR), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles ConstraintViolationException and constructs a detailed error response.
     *
     * @param exception the ConstraintViolationException thrown when a constraint is violated
     * @return ResponseEntity containing UserResponseDTO with error details and HTTP status BAD_REQUEST
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<UserResponseDTO> handleConstraintViolationException(ConstraintViolationException exception) {
        List<ErrorResponse> errors = new ArrayList<>();
        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        constraintViolations.forEach(constraintViolation -> {
            Path.Node lastNode = StreamSupport.stream(constraintViolation.getPropertyPath().spliterator(), false)
                    .reduce((first, second) -> second).orElse(null);
            errors.add(new ErrorResponse(constraintViolation.getMessage(), Objects.nonNull(lastNode) ? lastNode.getName() : null));
        });
        return new ResponseEntity<>(UserRequestResponseBuilder.buildResponseDTO(null, errors, ECommerceAppConstant.ERROR), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles ExpiredJwtException and returns a response indicating the token has expired.
     *
     * @param exception the ExpiredJwtException thrown when the JWT token is expired
     * @return a ResponseEntity containing a UserResponseDTO with error details and an UNAUTHORIZED status
     */
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<UserResponseDTO> handleExpiredJwtException(ExpiredJwtException exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), ECommerceAppConstant.TOKEN_EXPIRED_CODE);
        List<ErrorResponse> errors = new ArrayList<>();
        errors.add(errorResponse);
        return new ResponseEntity<>(UserRequestResponseBuilder.buildResponseDTO(null, errors, ECommerceAppConstant.ERROR), HttpStatus.UNAUTHORIZED);
    }
}
