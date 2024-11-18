package com.bilvantis.ecommerce.controller;

import com.bilvantis.ecommerce.api.service.UserService;
import com.bilvantis.ecommerce.model.UserResponseDTO;
import com.bilvantis.ecommerce.util.ECommerceAppConstant;
import com.bilvantis.ecommerce.util.UserRequestResponseBuilder;
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


    /**
     * Creates a new user.
     *
     * @param userDTO the UserDTO object containing user details, must not be null and must be valid
     * @return ResponseEntity containing the UserResponseDTO and HTTP status CREATED
     */
    @Validated(OnCreate.class)
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> createUser(@NotNull @Valid @RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(UserRequestResponseBuilder.buildResponseDTO(
                userService.createUser(userDTO), null, ECommerceAppConstant.SUCCESS), HttpStatus.CREATED);
    }

    /**
     * Retrieves a user by their unique user ID.
     *
     * @param id the unique identifier of the user, must not be null
     * @return ResponseEntity containing the UserResponseDTO and HTTP status OK
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserByUserId(@NotNull @PathVariable UUID id) {
        UserDTO userDTO = userService.getUserByUserId(id.toString());
        return new ResponseEntity<>(UserRequestResponseBuilder.buildResponseDTO(
                userDTO, null, ECommerceAppConstant.SUCCESS), HttpStatus.OK);
    }

    /**
     * Retrieves all users.
     *
     * @return ResponseEntity containing a list of UserDTOs and HTTP status OK
     */
    @GetMapping("/users")
    public ResponseEntity<UserResponseDTO> getAllUsers() {
        List<UserDTO> userDTOList = userService.getAllUsers();
        return new ResponseEntity<>(UserRequestResponseBuilder.buildResponseDTO(
                userDTOList, null, ECommerceAppConstant.SUCCESS),
                HttpStatus.OK);
    }

    /**
     * Updates an existing user.
     *
     * @param id      the unique identifier of the user, must not be null
     * @param userDTO the UserDTO object containing updated user details, must not be null and must be valid
     * @return ResponseEntity containing the updated UserResponseDTO and HTTP status OK
     */
    @Validated(OnUpdate.class)
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@NotNull @PathVariable String id,
                                                      @NotNull @Valid @RequestBody UserDTO userDTO) {
        UserDTO updatedUserDTO = userService.updateUserByUserId(id, userDTO);
        return new ResponseEntity<>(UserRequestResponseBuilder.buildResponseDTO(
                updatedUserDTO, null, ECommerceAppConstant.SUCCESS), HttpStatus.OK);
    }

    /**
     * Deletes a user by their unique user ID.
     *
     * @param id the unique identifier of the user, must not be null
     * @return ResponseEntity containing the status of the operation and HTTP status NO_CONTENT
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponseDTO> deleteUser(@NotNull @PathVariable String id) {
        userService.deleteUserByUserId(id);
        return new ResponseEntity<>(UserRequestResponseBuilder.buildResponseDTO(
                null, null, ECommerceAppConstant.SUCCESS), HttpStatus.NO_CONTENT);
    }
}
