package com.krishna.banking.auth.controller;


import com.krishna.banking.auth.dto.*;
import com.krishna.banking.auth.service.AuthService;
import com.krishna.banking.auth.service.RefreshTokenCookieService;
import com.krishna.banking.common.exceptions.InvalidRefreshTokenException;
import com.krishna.banking.user.dto.UserResponseDto;
import com.krishna.banking.user.security.CurrentUser;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenCookieService refreshTokenCookieService;
    private final CurrentUser currentUser;

    public AuthController(AuthService authService,
                          RefreshTokenCookieService refreshTokenCookieService,
                          CurrentUser currentUser){
        this.authService = authService;
        this.refreshTokenCookieService = refreshTokenCookieService;
        this.currentUser = currentUser;
    }

    @GetMapping("/csrf")
    public CsrfToken csrf(CsrfToken csrfToken){
        return csrfToken;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(
            @Valid @RequestBody RegisterRequestDto requestDto
            ){
        UserResponseDto userResponseDto = authService.register(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);


    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(
            @CookieValue(value ="refreshToken",required = false)
            String refreshToken){

        if (refreshToken == null || refreshToken.isBlank())
            throw new InvalidRefreshTokenException(
                    "Refresh token is missing"
            );

        LoginTokenResult loginTokenResult =
                authService.refresh(refreshToken);

        String accessToken =
                loginTokenResult.accessToken();

        ResponseCookie responseCookie =
                refreshTokenCookieService
                        .createRefreshTokenCookie(loginTokenResult.refreshToken());

        return ResponseEntity.ok()
                .header(
                    HttpHeaders.SET_COOKIE,
                    responseCookie.toString()
                )
                .body(
                        new LoginResponseDto(
                        accessToken,
                "Bearer")
                );

    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @Valid @RequestBody LoginRequestDto requestDto
    ){
         LoginTokenResult loginTokenResult = authService.login(requestDto);

        ResponseCookie refreshCookie =
                refreshTokenCookieService.
                        createRefreshTokenCookie(loginTokenResult.refreshToken());


         return ResponseEntity.
                 status(HttpStatus.OK)
                 .header(
                         HttpHeaders.SET_COOKIE,
                         refreshCookie.toString()
                 )
                 .body(new LoginResponseDto(
                 loginTokenResult.accessToken(),
                 "Bearer"
         ));
    }



    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(value = "refreshToken",required = false) String refreshToken
     ) {
        if(refreshToken != null){
            authService.logout(refreshToken);
        }

        ResponseCookie deleteCookie =
                refreshTokenCookieService.deleteRefreshTokenCookie();

        return ResponseEntity
                .noContent()
                .header(
                        HttpHeaders.SET_COOKIE,
                        deleteCookie.toString()
                )
                .build();

    }

    @PostMapping("/logout-all")
    public ResponseEntity<Void> logoutAll(){

        Long userId = currentUser.getId();
        authService.logoutAll(userId);

        ResponseCookie deleteCookie =
                refreshTokenCookieService.deleteRefreshTokenCookie();

        return ResponseEntity
                .noContent()
                .header(
                        HttpHeaders.SET_COOKIE,
                        deleteCookie.toString()
                )
                .build();
    }
}
