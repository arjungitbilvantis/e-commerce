package com.bilvantis.ecommerce.app.services.controller;

import com.bilvantis.ecommerce.api.service.LoginService;
import com.bilvantis.ecommerce.app.services.model.UserResponseDTO;
import com.bilvantis.ecommerce.app.services.util.ECommerceAppConstant;
import com.bilvantis.ecommerce.app.services.util.UserRequestResponseBuilder;
import com.bilvantis.ecommerce.dto.model.UserDTO;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/login")
@Validated
public class LoginController {


    @Qualifier("loginServiceImpl")
    private final LoginService<UserDTO> loginService;

    public LoginController(LoginService<UserDTO> loginService) {
        this.loginService = loginService;
    }

    /**
     * Verifies the login details (phone number and OTP) provided by the user.
     *
     * @param phoneNumber The user's phone number (cannot be null or empty).
     * @param otp         The OTP provided by the user (cannot be null or empty).
     * @return A ResponseEntity containing a UserResponseDTO if login is successful.
     */
    @GetMapping
    public ResponseEntity<UserResponseDTO> verifyLoginDetails(@NotNull @RequestParam String phoneNumber, @NotNull @RequestParam String otp) {
        return new ResponseEntity<>(UserRequestResponseBuilder.buildResponseDTO(loginService.verifyUserLogin(phoneNumber, otp), null, ECommerceAppConstant.SUCCESS), HttpStatus.OK);
    }

    /**
     * Sends a one-time password (OTP) to the user’s phone number.
     *
     * @param phoneNumber The phone number of the user to receive the OTP (cannot be null or empty).
     * @return A ResponseEntity containing a UserResponseDTO with the OTP sending status.
     */
    @PostMapping("/one-time-password")
    public ResponseEntity<UserResponseDTO> sendUserOneTimePassword(@NotNull @RequestParam String phoneNumber) {

        return new ResponseEntity<>(UserRequestResponseBuilder.buildResponseDTO(loginService.sendOneTimePasswordMail(phoneNumber), null, ECommerceAppConstant.SUCCESS), HttpStatus.OK);

    }

}
