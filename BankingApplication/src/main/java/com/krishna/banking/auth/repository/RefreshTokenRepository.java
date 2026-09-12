package com.krishna.banking.auth.repository;

import com.krishna.banking.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {

    Optional<RefreshToken> findByTokenHash(String tokenHash);
}
