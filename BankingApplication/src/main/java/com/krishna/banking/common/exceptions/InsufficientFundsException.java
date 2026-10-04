package com.krishna.banking.common.exceptions;

public class InsufficientFundsException extends  RuntimeException{

    public InsufficientFundsException(String message){
        super(message);
    }
}
