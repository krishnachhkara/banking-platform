package com.krishna.banking.user.service;

import com.krishna.banking.common.exceptions.UserNotFoundException;
import com.krishna.banking.user.dto.UserResponseDto;
import com.krishna.banking.user.entity.User;
import com.krishna.banking.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDto getCurrentUser(Long id){
        User user = userRepository
                .findById(id)
                .orElseThrow(()->
                        new UserNotFoundException("User doesn't exist"));

        return mapToDto(user);

    }

    private UserResponseDto mapToDto(User user){
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getStatus(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
