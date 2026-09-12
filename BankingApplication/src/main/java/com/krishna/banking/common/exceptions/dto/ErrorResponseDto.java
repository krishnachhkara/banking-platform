package com.krishna.banking.common.exceptions.dto;

import java.time.Instant;
import java.util.Map;

public record ErrorResponseDto(
        int status,
        String message,
        Instant timestamp,
        Map<String,String> fieldErrors
) {
}
