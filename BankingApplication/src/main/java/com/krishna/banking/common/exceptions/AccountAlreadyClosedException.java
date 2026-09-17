package com.krishna.banking.common.exceptions;

public class AccountAlreadyClosedException extends RuntimeException{

    public AccountAlreadyClosedException(String message){
        super(message);
    }
}
