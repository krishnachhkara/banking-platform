package com.krishna.banking.common.exceptions;

public class AccountAlreadyExistsException extends RuntimeException {

    public AccountAlreadyExistsException(String message){
        super(message);
    }
}
