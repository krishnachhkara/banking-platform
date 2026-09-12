package com.krishna.banking.auth.dto;

public record LoginResponseDto(
        String accessToken,
        String tokenType
) {
}
