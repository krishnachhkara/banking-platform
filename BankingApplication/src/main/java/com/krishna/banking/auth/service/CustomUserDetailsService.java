package com.krishna.banking.auth.service;

import com.krishna.banking.user.entity.User;
import com.krishna.banking.user.repository.UserRepository;
import com.krishna.banking.user.security.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(()->new UsernameNotFoundException("User doesn't exist"));

        return new CustomUserDetails(user);
    }
}
