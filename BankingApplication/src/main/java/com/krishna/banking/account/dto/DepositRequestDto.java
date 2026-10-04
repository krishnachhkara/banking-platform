package com.krishna.banking.account.dto;

import com.krishna.banking.transaction.entity.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record DepositRequestDto(
        @NotNull
        @DecimalMin("0.01")
        BigDecimal amount,


        @NotNull
        PaymentMethod paymentMethod
) {



}
