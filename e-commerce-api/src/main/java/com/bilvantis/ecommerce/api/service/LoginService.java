package com.bilvantis.ecommerce.api.service;

import com.bilvantis.ecommerce.dto.model.UserDTO;

public interface LoginService<I extends UserDTO> {

    String verifyUserLogin(String phoneNumber,String otp);

    String sendOneTimePasswordMail(String phoneNumber);
}
