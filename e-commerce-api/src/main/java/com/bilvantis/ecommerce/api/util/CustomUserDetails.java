package com.bilvantis.ecommerce.api.util;

import com.bilvantis.ecommerce.dao.data.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails extends User implements UserDetails {
    public CustomUserDetails(User user) {
        super();
        this.setUserId(user.getUserId());
        this.setPhoneNumber(user.getPhoneNumber());
        this.setFirstName(user.getFirstName());
        this.setLastName(user.getLastName());
        this.setEmail(user.getEmail());
        this.setPassword(user.getPassword());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // You can return roles or authorities for your user, depending on your business logic
        return Collections.singletonList(new SimpleGrantedAuthority("USER")); // For example
    }

    @Override
    public String getUsername() {
        return this.getPhoneNumber(); // Assuming phone number is the username
    }

    @Override
    public String getPassword() {
        return this.getPassword(); // Returning the password for Spring Security's checks
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // You can implement this based on your use case
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // You can implement this based on your use case
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // You can implement this based on your use case
    }

    @Override
    public boolean isEnabled() {
        return true; // You can implement this based on your use case
    }
}
