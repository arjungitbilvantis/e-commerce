package com.bilvantis.ecommerce.api.service.impl;

import com.bilvantis.ecommerce.api.exception.ApplicationException;
import com.bilvantis.ecommerce.api.exception.ResourceNotFoundException;
import com.bilvantis.ecommerce.api.service.EmailService;
import com.bilvantis.ecommerce.api.service.LoginService;
import com.bilvantis.ecommerce.api.service.UserService;
import com.bilvantis.ecommerce.api.util.*;
import com.bilvantis.ecommerce.dao.data.model.User;
import com.bilvantis.ecommerce.dao.data.repository.UserRepository;
import com.bilvantis.ecommerce.dto.model.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static com.bilvantis.ecommerce.api.util.LoginConstants.*;
import static com.bilvantis.ecommerce.api.util.UserSupport.convertUserEntityToUserDTO;

@Service("loginServiceImpl")
@Slf4j
public class LoginServiceImpl implements LoginService<UserDTO> {

    private final UserRepository userRepository;

    private final ECommerceProperties eCommerceProperties;

    private final EmailService emailService;


    private final UserService<UserDTO, UUID> userService;
    private final JwtUtil jwtUtil;

    public LoginServiceImpl(UserRepository userRepository, ECommerceProperties eCommerceProperties, EmailService emailService, UserService<UserDTO, UUID> userService, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.eCommerceProperties = eCommerceProperties;
        this.emailService = emailService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }


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


    @Override
    public String sendOneTimePasswordMail(String phoneNumber) {
        try {
            if (!Predicates.isValidPhoneNumber.test(phoneNumber)) {
                throw new ApplicationException(INVALID_PHONE_NUMBER);
            }
            Optional<User> userOptional = userRepository.findByPhoneNumber(phoneNumber);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                Random random = new Random();
                String randomSixDigit = String.format(eCommerceProperties.getOtpFormatPercentage(), random.nextInt(Integer.parseInt(eCommerceProperties.getOtpNoOfDigitsBound())));
                user.setOtp(randomSixDigit);
                user.setOtpGenerationTime(System.currentTimeMillis());
                UserDTO userDTO = convertUserEntityToUserDTO(user);
                EmailDetails emailDetails = EmailSupport.settingEmailDetails(user.getEmail(), eCommerceProperties.getSubjectForOtpGeneration());
                emailService.sendMailOtpGeneration(emailDetails, user);
                UserDTO updateUserDTO = userService.updateUserByUserId(userDTO.getUserId(), userDTO);
                return eCommerceProperties.getUserOtpSentViaMail();
            }
            throw new ApplicationException(USER_NOT_AVAILABLE);
        } catch (DataAccessException e) {
            throw new ApplicationException(e.getMessage());
        }
    }
}
