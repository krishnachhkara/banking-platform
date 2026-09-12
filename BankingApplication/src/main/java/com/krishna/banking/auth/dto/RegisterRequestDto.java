package com.krishna.banking.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDto(
        @NotBlank
        @Size(max = 50)
        String name,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min=12)
        String password

) {
}
