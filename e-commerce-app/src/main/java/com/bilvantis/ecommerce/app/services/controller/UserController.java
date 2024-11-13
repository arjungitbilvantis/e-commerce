package com.bilvantis.ecommerce.app.services.controller;

import com.bilvantis.ecommerce.api.service.UserService;
import com.bilvantis.ecommerce.app.services.model.UserResponseDTO;
import com.bilvantis.ecommerce.app.services.util.ECommerceAppConstant;
import com.bilvantis.ecommerce.app.services.util.UserRequestResponseBuilder;
import com.bilvantis.ecommerce.dto.model.UserDTO;
import com.bilvantis.ecommerce.dto.util.OnCreate;
import com.bilvantis.ecommerce.dto.util.OnUpdate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/user")
public class UserController {


    private final UserService<UserDTO, UUID> userService;

    public UserController(UserService<UserDTO, UUID> userService) {
        this.userService = userService;
    }


    @Validated(OnCreate.class)
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@NotNull @Valid @RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(UserRequestResponseBuilder.buildResponseDTO(
                userService.createUser(userDTO),null, ECommerceAppConstant.SUCCESS), HttpStatus.CREATED);
    }

    // Get User by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserByUserId(@NotNull @PathVariable UUID id) {
        UserDTO userDTO = userService.getUserByUserId(id.toString());
        return new ResponseEntity<>(UserRequestResponseBuilder.buildResponseDTO(
                userDTO, null, ECommerceAppConstant.SUCCESS), HttpStatus.OK);
    }

    // Get All Users
    @GetMapping
    public ResponseEntity<UserResponseDTO> getAllUsers() {
        List<UserDTO> userDTOList = userService.getAllUsers();
        return new ResponseEntity<>(UserRequestResponseBuilder.buildResponseDTO(
                userDTOList, null, ECommerceAppConstant.SUCCESS),
                HttpStatus.OK);
    }

    // Update User by ID
    @Validated(OnUpdate.class)
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@NotNull @PathVariable String id,
                                                      @NotNull @Valid @RequestBody UserDTO userDTO) {
        UserDTO updatedUserDTO = userService.updateUserByUserId(id, userDTO);
        return new ResponseEntity<>(UserRequestResponseBuilder.buildResponseDTO(
                updatedUserDTO, null, ECommerceAppConstant.SUCCESS), HttpStatus.OK);
    }

    // Delete User by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponseDTO> deleteUser(@NotNull @PathVariable String id) {
        userService.deleteUserByUserId(id);
        return new ResponseEntity<>(UserRequestResponseBuilder.buildResponseDTO(
                null, null, ECommerceAppConstant.SUCCESS), HttpStatus.NO_CONTENT);
    }
}
