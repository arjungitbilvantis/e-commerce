package com.bilvantis.ecommerce.app.services.util;

import com.bilvantis.ecommerce.dao.data.model.User;
import com.bilvantis.ecommerce.dao.util.UserType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserDetailsImpl implements UserDetails {

    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Map the UserType to authorities (Roles)
        if (user.getUserType() == UserType.ADMIN) {
            return Collections.singletonList(new SimpleGrantedAuthority("ADMIN"));
        } else {
            return Collections.singletonList(new SimpleGrantedAuthority("USER"));
        }
    }

    @Override
    public String getPassword() {
        // Return password from user, assuming your User entity has a password field
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        // Return the phone number from the user (this is how the token is generated and validated)
        return user.getPhoneNumber();
    }

    @Override
    public boolean isAccountNonExpired() {
        // You can implement actual logic for account expiration if needed
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // You can implement actual logic for account locking if needed
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // You can implement actual logic for credential expiration if needed
        return true;
    }

    @Override
    public boolean isEnabled() {
        // You can implement actual logic for enabling/disabling the account if needed
        return true;
    }
}
