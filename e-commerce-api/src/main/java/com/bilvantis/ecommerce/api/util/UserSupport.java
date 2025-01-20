package com.bilvantis.ecommerce.api.util;

import com.bilvantis.ecommerce.dao.data.model.User;
import com.bilvantis.ecommerce.dao.util.UserType;
import com.bilvantis.ecommerce.dto.model.UserDTO;
import com.bilvantis.ecommerce.dto.model.UserTypeDTO;

import java.util.Objects;

public class UserSupport {

    /**
     * Converts a User entity to a UserDTO object.
     *
     * @param user the User entity containing the user data
     * @return a UserDTO object populated with the data from the User entity
     */
    public static UserDTO convertUserEntityToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setGender(user.getGender());
        // Mapping UserType enum to UserTypeDTO enum
        if (Objects.nonNull(user.getUserType())) {
            // Convert UserType to UserTypeDTO
            userDTO.setUserTypeDTO(UserTypeDTO.valueOf(user.getUserType().name()));
        }
        userDTO.setAddress(user.getAddress());
        userDTO.setFirstTimeUser(user.getFirstTimeUser());
        userDTO.setIsActive(user.getIsActive());
        userDTO.setCreatedBy(user.getCreatedBy());
        userDTO.setCreatedDate(Objects.nonNull(user.getCreatedDate()) ? user.getCreatedDate() : null);
        userDTO.setUpdatedBy(user.getUpdatedBy());
        userDTO.setUpdatedDate(Objects.nonNull(user.getUpdatedDate()) ? user.getUpdatedDate() : null);
        return userDTO;
    }

    /**
     * Converts a UserDTO object to a User entity.
     *
     * @param userDTO the UserDTO object containing the user data
     * @return a User entity populated with the data from the UserDTO
     */
    public static User convertUserDTOToUserEntity(UserDTO userDTO) {
        User user = new User();
        user.setUserId(userDTO.getUserId());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setGender(userDTO.getGender());
        // Convert UserTypeDTO to UserType enum
        if (Objects.nonNull(userDTO.getUserTypeDTO())) {
            // Convert UserTypeDTO to UserType
            user.setUserType(UserType.valueOf(userDTO.getUserTypeDTO().name()));
        }
        user.setAddress(userDTO.getAddress());
        user.setFirstTimeUser(userDTO.getFirstTimeUser());
        user.setIsActive(Boolean.TRUE);
        user.setCreatedBy(userDTO.getCreatedBy());
        user.setCreatedDate(Objects.nonNull(userDTO.getCreatedDate()) ? userDTO.getCreatedDate() : null);
        user.setUpdatedBy(userDTO.getUpdatedBy());
        user.setUpdatedDate(Objects.nonNull(userDTO.getUpdatedDate()) ? userDTO.getUpdatedDate() : null);
        return user;
    }

    /**
     * Updates the fields of an existing User object based on the values from a UserDTO object.
     *
     * @param existingUser the User object to be updated
     * @param userDTO      the UserDTO object containing the new values
     */
    public static void updateUserFromDTO(User existingUser, UserDTO userDTO) {
        // Update the user fields based on UserDTO values
        existingUser.setFirstName(userDTO.getFirstName());
        existingUser.setLastName(userDTO.getLastName());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setPhoneNumber(userDTO.getPhoneNumber());
        existingUser.setGender(userDTO.getGender());
        if (Objects.nonNull(userDTO.getUserTypeDTO())) {
            // Convert UserTypeDTO to UserType enum and set it
            existingUser.setUserType(UserType.valueOf(userDTO.getUserTypeDTO().name()));
        }
        existingUser.setAddress(userDTO.getAddress());
        existingUser.setFirstTimeUser(userDTO.getFirstTimeUser());
        existingUser.setUpdatedBy(userDTO.getUpdatedBy());
        existingUser.setUpdatedDate(userDTO.getUpdatedDate());
    }
}
