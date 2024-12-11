package com.bilvantis.ecommerce.util;

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

    /**
     * Returns the authorities (roles) granted to the user.
     *
     * @return a collection of granted authorities based on the user's type
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Map the UserType to authorities (Roles)
        if (user.getUserType() == UserType.ADMIN) {
            return Collections.singletonList(new SimpleGrantedAuthority("ADMIN"));
        } else {
            return Collections.singletonList(new SimpleGrantedAuthority("USER"));
        }
    }

    /**
     * Returns the password of the user.
     *
     * @return the user's password
     */
    @Override
    public String getPassword() {
        // Return password from user, assuming your User entity has a password field
        return user.getPassword();
    }

    /**
     * Returns the username of the user.
     * In this case, it returns the phone number of the user.
     *
     * @return the user's phone number
     */
    @Override
    public String getUsername() {
        // Return the phone number from the user (this is how the token is generated and validated)
        return user.getPhoneNumber();
    }

    /**
     * Indicates whether the user's account has expired.
     *
     * @return true if the user's account is non-expired, false otherwise
     */
    @Override
    public boolean isAccountNonExpired() {
        // You can implement actual logic for account expiration if needed
        return true;
    }

    /**
     * Indicates whether the user is locked or unlocked.
     *
     * @return true if the user's account is non-locked, false otherwise
     */
    @Override
    public boolean isAccountNonLocked() {
        // You can implement actual logic for account locking if needed
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) have expired.
     *
     * @return true if the user's credentials are non-expired, false otherwise
     */
    @Override
    public boolean isCredentialsNonExpired() {
        // You can implement actual logic for credential expiration if needed
        return true;
    }

    /**
     * Indicates whether the user is enabled or disabled.
     *
     * @return true if the user is enabled, false otherwise
     */
    @Override
    public boolean isEnabled() {
        // You can implement actual logic for enabling/disabling the account if needed
        return true;
    }

}
