package com.krishna.banking.account.dto;

import com.krishna.banking.account.entity.AccountType;
import jakarta.validation.constraints.NotNull;

public record CreateAccountRequestDto(
        @NotNull
        AccountType accountType
) {

}
