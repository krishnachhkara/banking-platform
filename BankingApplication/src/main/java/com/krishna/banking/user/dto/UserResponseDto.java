package com.krishna.banking.user.dto;

import com.krishna.banking.user.entity.Role;
import com.krishna.banking.user.entity.UserStatus;

import java.time.Instant;

public record UserResponseDto(
        Long id,
        String name,
        String email,
        Role role,
        UserStatus status,
        Instant createdAt,
        Instant updatedAt
) {
}
