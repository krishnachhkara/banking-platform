package com.krishna.banking.transaction.dto;

import com.krishna.banking.transaction.entity.PaymentMethod;
import com.krishna.banking.transaction.entity.TransactionStatus;
import com.krishna.banking.transaction.entity.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionResponseDto(
        Long transactionId,
        TransactionType transactionType,
        TransactionStatus transactionStatus,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        String senderAccountNumber,
        String receiverAccountNumber,
        Instant createdAt
) {
}
