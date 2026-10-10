package com.krishna.banking.common.exceptions;

public class InvalidPaginationParameterException extends RuntimeException{

    public InvalidPaginationParameterException(String message){
        super(message);
    }
}
