package com.bilvantis.ecommerce.api.service.impl;

import com.bilvantis.ecommerce.api.exception.ApplicationException;
import com.bilvantis.ecommerce.api.exception.ResourceNotFoundException;
import com.bilvantis.ecommerce.api.service.EmailService;
import com.bilvantis.ecommerce.api.service.LoginService;
import com.bilvantis.ecommerce.api.service.UserService;
import com.bilvantis.ecommerce.api.util.EmailDetails;
import com.bilvantis.ecommerce.api.util.EmailSupport;
import com.bilvantis.ecommerce.api.util.JwtUtil;
import com.bilvantis.ecommerce.api.util.Predicates;
import com.bilvantis.ecommerce.dao.data.model.User;
import com.bilvantis.ecommerce.dao.data.repository.UserRepository;
import com.bilvantis.ecommerce.dto.model.UserDTO;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static com.bilvantis.ecommerce.api.util.LoginConstants.*;

@Service("loginServiceImpl")
@Slf4j
public class LoginServiceImpl implements LoginService<UserDTO> {

    private final UserRepository userRepository;

    private final EmailService emailService;

    private final JwtUtil jwtUtil;

    public LoginServiceImpl(UserRepository userRepository, EmailService emailService, UserService<UserDTO, UUID> userService, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.jwtUtil = jwtUtil;
    }


    /**
     * Verifies the user's login by checking the provided phone number and OTP.
     *
     * @param phoneNumber the phone number of the user
     * @param otp         the one-time password provided by the user
     * @return a JWT token if the login is successful
     * @throws ResourceNotFoundException if the phone number or OTP is empty, or if the user is not found
     * @throws ApplicationException      if there is a mismatch between the phone number and OTP, or if there is a database access error or unexpected error
     */
    @Override
    public String verifyUserLogin(String phoneNumber, String otp) {
        try {
            // Validate phoneNumber
            if (StringUtils.isEmpty(phoneNumber)) {
                log.error(LOG_PHONE_NUMBER_EMPTY_OR_NULL);
                throw new ResourceNotFoundException(PHONE_NUMBER_EMPTY_OR_NULL);
            }

            // Validate OTP
            if (StringUtils.isEmpty(otp)) {
                log.error(LOG_OTP_EMPTY_OR_NULL);
                throw new ResourceNotFoundException(OTP_EMPTY_OR_NULL);
            }

            // Fetch user by phone number
            Optional<User> userOptional = userRepository.findByPhoneNumber(phoneNumber);
            if (userOptional.isEmpty()) {
                log.error(LOG_USER_NOT_FOUND, phoneNumber);
                throw new ResourceNotFoundException(USER_NOT_FOUND_FOR_PHONE_NUMBER);
            }

            User user = userOptional.get();

            // Check if OTP matches
            if (StringUtils.equals(user.getOtp(), otp)) {
                // Generate a JWT token
                return jwtUtil.generateToken(user); // Return the token to the client
            } else {
                log.error(LOG_PHONE_NUMBER_OTP_MISMATCH);
                throw new ApplicationException(PHONE_NUMBER_OTP_MISMATCH);
            }
        } catch (DataAccessException e) {
            log.error(LOG_DATABASE_ACCESS_ERROR, e.getMessage());
            throw new ApplicationException(DATABASE_ACCESS_ERROR + e.getMessage());
        } catch (Exception e) {
            log.error(LOG_UNEXPECTED_ERROR, e.getMessage());
            throw new ApplicationException(UNEXPECTED_ERROR + e.getMessage());
        }
    }

    /**
     * Sends a one-time password (OTP) to the user's email based on their phone number.
     *
     * @param phoneNumber the phone number of the user
     * @return a message indicating that the OTP has been sent successfully
     * @throws ApplicationException if the phone number is invalid, the user is not available, or there is a data access error
     */
    @Transactional
    @Override
    public String sendOneTimePasswordMail(String phoneNumber) {
        try {
            if (!Predicates.isValidPhoneNumber.test(phoneNumber)) {
                throw new ApplicationException(INVALID_PHONE_NUMBER);
            }

            Optional<User> userOptional = userRepository.findByPhoneNumber(phoneNumber);
            if (userOptional.isPresent()) {
                User user = userOptional.get();

                // Generate a 6-digit random OTP
                String randomSixDigit = String.format(OTP_FORMAT, new Random().nextInt(1000000));

                user.setOtp(randomSixDigit);
                user.setOtpGenerationTime(System.currentTimeMillis());

                // Prepare email details and send the OTP
                EmailDetails emailDetails = EmailSupport.settingEmailDetails(
                        user.getEmail(), OTP_REQUEST_SUBJECT);
                emailService.sendMailOtpGeneration(emailDetails, user);

                // Update OTP in the database
                userRepository.updateOtpByUserId(user.getUserId(), randomSixDigit, System.currentTimeMillis());
                return OTP_SENT_MESSAGE;
            }
            throw new ApplicationException(USER_NOT_AVAILABLE);
        } catch (DataAccessException e) {
            throw new ApplicationException(e.getMessage());
        }
    }

}
