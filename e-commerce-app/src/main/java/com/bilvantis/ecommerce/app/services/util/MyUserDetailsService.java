package com.bilvantis.ecommerce.app.services.util;

import com.bilvantis.ecommerce.dao.data.model.User;
import com.bilvantis.ecommerce.dao.data.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.bilvantis.ecommerce.app.services.util.ECommerceAppConstant.USER_NOT_FOUND;


@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, phoneNumber)));
        return new UserDetailsImpl(user);
    }
}
