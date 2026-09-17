package com.krishna.banking.account.dto;

import com.krishna.banking.account.entity.AccountStatus;
import com.krishna.banking.account.entity.AccountType;

import java.math.BigDecimal;
import java.time.Instant;

public record AccountResponseDto(
        String accountNumber,
        AccountType accountType,
        BigDecimal balance,
        AccountStatus status,
        Instant createdAt
) {
}
