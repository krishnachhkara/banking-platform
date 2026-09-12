package com.krishna.banking.auth.service;

import com.krishna.banking.auth.dto.*;
import com.krishna.banking.auth.entity.RefreshToken;
import com.krishna.banking.common.exceptions.EmailAlreadyExistsException;
import com.krishna.banking.user.dto.UserResponseDto;
import com.krishna.banking.user.entity.Role;
import com.krishna.banking.user.entity.User;
import com.krishna.banking.user.entity.UserStatus;
import com.krishna.banking.user.repository.UserRepository;
import com.krishna.banking.user.security.CustomUserDetails;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Locale;


@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;
    private final String jwtIssuer;
    private final RefreshTokenService refreshTokenService;


    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtEncoder jwtEncoder,
                       @Value("${jwt.issuer}") String jwtIssuer,
                       RefreshTokenService refreshTokenService,
                       RefreshTokenCookieService refreshTokenCookieService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
        this.jwtIssuer = jwtIssuer;
        this.refreshTokenService = refreshTokenService;
    }

    public UserResponseDto register(RegisterRequestDto registerRequestDto){
        String email = registerRequestDto.email().trim().toLowerCase(Locale.ROOT);

        if(userRepository.existsByEmail(email)){
            throw new EmailAlreadyExistsException("Email already exists");
        }

        String passwordHash = passwordEncoder.encode(registerRequestDto.password());

        User user = new User();

        user.setName(registerRequestDto.name());
        user.setEmail(email);
        user.setPasswordHash(passwordHash);
        user.setRole(Role.CUSTOMER);
        user.setStatus(UserStatus.ACTIVE);

        User savedUser = userRepository.save(user);
        return mapToDto(savedUser);
    }

    public LoginTokenResult login(LoginRequestDto requestDto)
    {

        String email = requestDto.email().trim().toLowerCase(Locale.ROOT);
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken
                        (email,requestDto.password());

        Authentication authentication = authenticationManager.authenticate(token);

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();


        //access Token
        String accessToken =
                generateAccessToken(userDetails.getUserId(),userDetails.getRole());

        //refresh token

        String refreshToken =
                refreshTokenService.createRefreshToken(userDetails.getUserId());

        return new LoginTokenResult(
                accessToken,
                refreshToken
        );

    }

    private String generateAccessToken(Long id, Role role){
        Instant now = Instant.now();

        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .subject(String.valueOf(id))
                .claim("role",role)
                .issuer(jwtIssuer)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(15 * 60))
                .build();

        // creating the header by adding alg: HS256
        JwsHeader jwsHeader =
                JwsHeader.with(MacAlgorithm.HS256).build();

        // here and for above header .with and .from are static methods called using class name
        JwtEncoderParameters parameters =
                JwtEncoderParameters.from(jwsHeader,claimsSet);

        // get token value converts jwt object in string

               return jwtEncoder.encode(parameters).getTokenValue();
    }

    @Transactional
    public LoginTokenResult refresh(String rawRefreshToken){

        RefreshToken oldToken =
                refreshTokenService.validateRefreshToken(rawRefreshToken);

        refreshTokenService.revokeRefreshToken(oldToken);

        String newRefreshToken =
                refreshTokenService.rotateRefreshToken(oldToken);

        String newAccessToken =
                generateAccessToken(
                        oldToken.getUser().getId(),oldToken.getUser().getRole()
                );

        return new LoginTokenResult(
                newAccessToken,
                newRefreshToken
        );

    }

    @Transactional
    public void logout(String rawRefreshToken) {

        RefreshToken refreshToken =
                refreshTokenService.validateRefreshToken(rawRefreshToken);

        refreshTokenService.revokeRefreshToken(refreshToken);
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
