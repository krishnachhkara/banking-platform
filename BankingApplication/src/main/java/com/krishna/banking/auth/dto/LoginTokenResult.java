package com.krishna.banking.auth.dto;

public record LoginTokenResult(
        String accessToken,
        String refreshToken

)
{}
