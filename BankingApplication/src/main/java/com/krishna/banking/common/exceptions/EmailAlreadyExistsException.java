package com.krishna.banking.common.exceptions;

public class EmailAlreadyExistsException extends RuntimeException{


    public EmailAlreadyExistsException(String message){
        super(message);
    }
}
