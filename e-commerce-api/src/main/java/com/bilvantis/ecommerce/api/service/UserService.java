package com.bilvantis.ecommerce.api.service;

import com.bilvantis.ecommerce.dto.model.UserDTO;

import java.io.Serializable;
import java.util.List;

public interface UserService<I extends UserDTO,ID extends Serializable> {

    I createUser(I userDTO);

    I getUserByUserId(String uuid);

    List<I> getAllUsers();

    I updateUserByUserId(String userId,I userDTO);
    void deleteUserByUserId(String userId);
}
