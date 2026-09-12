package com.krishna.banking.common.exceptions;

public class InvalidRefreshTokenException
        extends RuntimeException {

    public InvalidRefreshTokenException(String message) {
        super(message);
    }
}