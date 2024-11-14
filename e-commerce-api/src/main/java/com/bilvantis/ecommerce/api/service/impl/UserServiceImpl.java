package com.bilvantis.ecommerce.api.service.impl;

import com.bilvantis.ecommerce.api.exception.ApplicationException;
import com.bilvantis.ecommerce.api.service.EmailService;
import com.bilvantis.ecommerce.api.service.UserService;
import com.bilvantis.ecommerce.api.util.EmailDetails;
import com.bilvantis.ecommerce.api.util.UserSupport;
import com.bilvantis.ecommerce.dao.data.model.User;
import com.bilvantis.ecommerce.dao.data.repository.UserRepository;
import com.bilvantis.ecommerce.dto.model.UserDTO;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.bilvantis.ecommerce.api.util.UserConstants.*;
import static com.bilvantis.ecommerce.api.util.UserSupport.*;

@Service("userServiceImpl")
public class UserServiceImpl implements UserService<UserDTO, UUID> {


    private final UserRepository userRepository;

    private final EmailService emailService;

    public UserServiceImpl(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    /**
     * Creates a new user based on the provided UserDTO.
     *
     * @param userDTO the UserDTO object containing the user data
     * @return the created UserDTO object
     * @throws ApplicationException if a user with the same phone number or email already exists,
     *                              or if there is an error during the save operation
     */
    @Override
    public UserDTO createUser(UserDTO userDTO) {
        try {
            // Check if a user with the same phone number already exists
            if (userRepository.existsByPhoneNumber(userDTO.getPhoneNumber())) {
                throw new ApplicationException(String.format(PHONE_NUMBER_EXISTS, userDTO.getPhoneNumber()));
            }

            // Alternatively, check if the email exists (if applicable)
            if (userRepository.existsByEmail(userDTO.getEmail())) {
                throw new ApplicationException(String.format(EMAIL_EXISTS, userDTO.getEmail()));
            }
            // Create a new user entity from the DTO
            User user = convertUserDTOToUserEntity(userDTO);

            // Generate a random UUID for userId before saving
            UUID generatedUserId = UUID.randomUUID();
            user.setUserId(generatedUserId.toString());

            // Generate and set the OTP for verification
            String otp = generateOtp();
            user.setOtp(otp);

            // Save the user entity
            User savedUser = userRepository.save(user);

            // Send verification email
            EmailDetails emailDetails = new EmailDetails();
            emailDetails.setRecipient(userDTO.getEmail());
            emailDetails.setSubject(REGISTRATION_SUCCESS_EMAIL_SUBJECT);
            emailService.sendMailOtpGeneration(emailDetails, savedUser);

            return convertUserEntityToUserDTO(savedUser);
        } catch (DataAccessException e) {
            throw new ApplicationException(USER_SAVE_FAILED);
        }
    }

    /**
     * Retrieves a user by their UUID.
     *
     * @param uuid the UUID of the user to retrieve
     * @return the UserDTO object representing the retrieved user
     * @throws ApplicationException if the user is not found or if there is an error during the fetch operation
     */
    @Override
    public UserDTO getUserByUserId(String uuid) {
        try {
            // Look up the user by UUID
            Optional<User> userOptional = userRepository.findById(uuid);

            if (userOptional.isPresent()) {
                // Convert entity to DTO and return it
                return convertUserEntityToUserDTO(userOptional.get());
            } else {
                throw new ApplicationException(String.format(USER_NOT_FOUND, uuid));
            }
        } catch (DataAccessException e) {
            throw new ApplicationException(USER_FETCH_FAILED);
        }
    }

    /**
     * Retrieves all active users.
     *
     * @return a list of `UserDTOs` representing all active users
     * @throws ApplicationException if no users are found or if there is a data access error
     */
    @Override
    public List<UserDTO> getAllUsers() {
        try {
            // Fetch all users from the repository
            List<User> users = userRepository.findAllActiveUsers();

            // Check if the list is empty and throw an exception if no users are found
            if (users.isEmpty()) {
                throw new ApplicationException(NO_USERS_FOUND);
            }

            // Convert the list of entities to a list of DTOs
            return convertUserEntityToUserDTOList(users);
        } catch (DataAccessException e) {
            throw new ApplicationException(USERS_FETCH_FAILED);
        }
    }

    /**
     * Updates a user by their user ID.
     *
     * @param userId  the ID of the user to be updated
     * @param userDTO the data transfer object containing the updated user details
     * @return the updated `UserDTO`
     * @throws ApplicationException if a user with the same phone number or email already exists,
     *                              if the user is not found, or if there is a data access error
     */
    @Override
    public UserDTO updateUserByUserId(String userId, UserDTO userDTO) {
        try {

            // Check if the provided userId matches the userDTO's userId
            if (!Objects.equals(userId, userDTO.getUserId())) {
                throw new ApplicationException(String.format(USER_ID_MISMATCH, userId, userDTO.getUserId()));
            }
            // Check if a user with the same phone number already exists
            if (userRepository.existsByPhoneNumber(userDTO.getPhoneNumber())) {
                throw new ApplicationException(String.format(PHONE_NUMBER_EXISTS, userDTO.getPhoneNumber()));
            }

            // Alternatively, check if the email exists (if applicable)
            if (userRepository.existsByEmail(userDTO.getEmail())) {
                throw new ApplicationException(String.format(EMAIL_EXISTS, userDTO.getEmail()));
            }
            // Check if the user exists first
            Optional<User> existingUserOptional = userRepository.findById(userId);

            if (existingUserOptional.isPresent()) {
                User existingUser = existingUserOptional.get();

                // Update the user fields using the helper method
                updateUserFromDTO(existingUser, userDTO);

                // Save the updated user entity
                return convertUserEntityToUserDTO(userRepository.save(existingUser));
            } else {
                throw new ApplicationException(String.format(USER_NOT_FOUND, userId));
            }
        } catch (DataAccessException e) {
            throw new ApplicationException(String.format(ERROR_UPDATING_USER, userId));
        }
    }

    /**
     * Soft Deletes a user by their user ID.
     *
     * @param userId the ID of the user to be deleted
     * @throws ApplicationException if the user is not found or if there is a data access error
     */
    @Override
    public void deleteUserByUserId(String userId) {
        try {
            // Check if user exists before deleting
            Optional<User> userOptional = userRepository.findById(userId);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setIsActive(Boolean.FALSE);
                userRepository.save(user);
            } else {
                throw new ApplicationException(String.format(USER_NOT_FOUND, userId));
            }
        } catch (DataAccessException e) {
            throw new ApplicationException(String.format(ERROR_DELETING_USER, userId));
        }
    }

    /**
     * Converts a list of User entities to a list of UserDTOs.
     *
     * @param users the list of User entities to be converted
     * @return a list of UserDTOs
     */
    private List<UserDTO> convertUserEntityToUserDTOList(List<User> users) {
        return users.stream()
                .map(UserSupport::convertUserEntityToUserDTO)
                .toList();
    }

    /**
     * Generates a 6-digit One-Time Password (OTP).
     *
     * @return a String representation of the 6-digit OTP
     */
    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // 6-digit OTP
        return String.valueOf(otp);
    }

}
