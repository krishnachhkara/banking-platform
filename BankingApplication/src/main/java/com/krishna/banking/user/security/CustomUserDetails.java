package com.krishna.banking.user.security;

import com.krishna.banking.user.entity.Role;
import com.krishna.banking.user.entity.User;
import com.krishna.banking.user.entity.UserStatus;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_"+user.getRole());
        return List.of(authority);
    }

    @Override
    public @Nullable String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isEnabled() {
        return user.getStatus() == UserStatus.ACTIVE;
    }

    //to get user id for subject in jwt claims
    public Long getUserId() {
        return user.getId();
    }

    //to get user role for claim in jwt claims
    public Role getRole() {
        return user.getRole();
    }
}
