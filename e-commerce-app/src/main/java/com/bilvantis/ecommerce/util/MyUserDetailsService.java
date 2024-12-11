package com.bilvantis.ecommerce.util;

import com.bilvantis.ecommerce.dao.data.model.User;
import com.bilvantis.ecommerce.dao.data.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads the user details by phone number.
     *
     * @param phoneNumber The phone number of the user to be loaded.
     * @return {@link UserDetails} an implementation of the UserDetails interface containing the user's information.
     */
    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(ECommerceAppConstant.USER_NOT_FOUND, phoneNumber)));
        return new UserDetailsImpl(user);
    }
}
