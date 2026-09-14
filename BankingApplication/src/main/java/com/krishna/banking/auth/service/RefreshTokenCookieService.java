package com.krishna.banking.auth.service;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RefreshTokenCookieService {


    public ResponseCookie createRefreshTokenCookie(String refreshToken){

        return ResponseCookie.from("refreshToken",refreshToken)
                        .httpOnly(true)
                        .secure(false)
                        .sameSite("lax")
                        .maxAge(Duration.ofDays(7))
                        .path("/auth")
                        .build();

    }

    public ResponseCookie deleteRefreshTokenCookie(){

        return ResponseCookie.from("refreshToken","")
                        .httpOnly(true)
                        .secure(false)
                        .sameSite("lax")
                        .maxAge(Duration.ZERO)
                        .path("/auth")
                        .build();

    }
}
