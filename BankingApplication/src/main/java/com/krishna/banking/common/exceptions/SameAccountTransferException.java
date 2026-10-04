package com.krishna.banking.common.exceptions;

public class SameAccountTransferException extends RuntimeException{

    public SameAccountTransferException(String message){
        super(message);
    }

}
