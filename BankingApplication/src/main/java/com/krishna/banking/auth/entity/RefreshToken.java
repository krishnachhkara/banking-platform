package com.krishna.banking.auth.entity;


import com.krishna.banking.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "refresh_tokens")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String tokenHash;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)// every refresh token must have a user is done by optional = false
    // and is for java not db and jpa translates this into db
    @JoinColumn(name = "user_id", nullable = false)// nullable false is for db to tell fkey cant be null
    private User user;

    @Column(nullable = false)
    private Instant expiresAt;

    @CreatedDate
    @Column(nullable = false,updatable = false)
    private Instant createdAt;

    private Instant revokedAt;
}
