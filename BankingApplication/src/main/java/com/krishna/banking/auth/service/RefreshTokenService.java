package com.krishna.banking.auth.service;


import com.krishna.banking.auth.entity.RefreshToken;
import com.krishna.banking.auth.repository.RefreshTokenRepository;
import com.krishna.banking.common.exceptions.InvalidRefreshTokenException;
import com.krishna.banking.common.exceptions.UserNotFoundException;
import com.krishna.banking.user.entity.User;
import com.krishna.banking.user.entity.UserStatus;
import com.krishna.banking.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HexFormat;
import java.util.List;


@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    //Later remove it for entitymanager.getreference to skip
    // one db call for create refresh token actually need user
    private final UserRepository userRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository,
                               UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public String createRefreshToken(Long userId) {
        Instant expiresAt = Instant.now()
                .plus(7, ChronoUnit.DAYS);

        return createRefreshToken(userId, expiresAt);
    }

    public String createRefreshToken(Long userId,Instant expiresAt)
             {
        String rawToken = generateRefreshToken();

        String tokenHash = hashRefreshToken(rawToken);
        User user = userRepository
                .findById(userId)
                .orElseThrow(()->
                        new UserNotFoundException("User doesn't exist"));

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpiresAt(expiresAt);
        refreshToken.setTokenHash(tokenHash);
        refreshToken.setUser(user);

        refreshTokenRepository.save(refreshToken);
        return rawToken;
    }

    private String generateRefreshToken(){
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);

        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String hashRefreshToken(String token) {

            try {
                MessageDigest digest =
                        MessageDigest.getInstance("SHA-256");

                byte[] hash =
                        digest.digest(
                                token.getBytes(StandardCharsets.UTF_8)
                        );

                return HexFormat.of().formatHex(hash);

            } catch (NoSuchAlgorithmException e) {
                throw new IllegalStateException(
                        "SHA-256 algorithm is not available",
                        e
                );
            }

    }

    public RefreshToken validateRefreshToken(String rawToken){
        String tokenHash = hashRefreshToken(rawToken);

        RefreshToken savedToken = refreshTokenRepository
                .findByTokenHash(tokenHash)
                .orElseThrow(()->
                        new InvalidRefreshTokenException("Invalid refresh token"));

        if(savedToken.getRevokedAt()!=null){
            throw new InvalidRefreshTokenException("Invalid refresh token");
        }

        Instant now = Instant.now();

        if(!savedToken.getExpiresAt().isAfter(now)){
            throw new InvalidRefreshTokenException("Invalid refresh token");
        }


        if(savedToken.getUser().getStatus()!= UserStatus.ACTIVE){
            throw new InvalidRefreshTokenException("Invalid refresh token");
        }

        return savedToken;

    }

    public void revokeRefreshToken(RefreshToken token){
        Instant now = Instant.now();
        token.setRevokedAt(now);
    }

    public String rotateRefreshToken(RefreshToken oldToken) {
        return createRefreshToken(
                oldToken.getUser().getId(),
                oldToken.getExpiresAt()
        );
    }


    @Transactional
    public void revokeAllRefreshToken(Long userId){
        List<RefreshToken> refreshTokenList =
                refreshTokenRepository.findByUserIdAndRevokedAtIsNull(userId);
        refreshTokenList.forEach(token-> revokeRefreshToken(token));
    }
}
