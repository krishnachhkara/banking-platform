package com.krishna.banking.common.exceptions;


import com.krishna.banking.common.exceptions.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleEmailAlreadyExistsException
            (EmailAlreadyExistsException ex) {
        ErrorResponseDto responseDto = new ErrorResponseDto(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                Instant.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(responseDto);

    }

    @ExceptionHandler(AccountAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleAccountAlreadyExistsException
            (AccountAlreadyExistsException ex) {
        ErrorResponseDto responseDto = new ErrorResponseDto(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                Instant.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(responseDto);

    }

    @ExceptionHandler(AccountAlreadyClosedException.class)
    public ResponseEntity<ErrorResponseDto> handleAccountAlreadyClosedException
            (AccountAlreadyClosedException ex) {

        ErrorResponseDto responseDto = new ErrorResponseDto(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                Instant.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(responseDto);

    }


    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ErrorResponseDto> handleInsufficientFundsException
            (InsufficientFundsException ex) {

        ErrorResponseDto responseDto = new ErrorResponseDto(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                Instant.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(responseDto);

    }
    @ExceptionHandler(SameAccountTransferException.class)
    public ResponseEntity<ErrorResponseDto> handleSameAccountTransferException
            (SameAccountTransferException ex) {

        ErrorResponseDto responseDto = new ErrorResponseDto(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                Instant.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(responseDto);

    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUserNotFoundException
            (UserNotFoundException ex) {
        ErrorResponseDto responseDto = new ErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                Instant.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(responseDto);

    }


    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleAccountNotFoundException
            (AccountNotFoundException ex) {
        ErrorResponseDto responseDto = new ErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                Instant.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(responseDto);

    }



    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidRefreshTokenException
            (InvalidRefreshTokenException ex) {

        ErrorResponseDto responseDto = new ErrorResponseDto(
                HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage(),
                Instant.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(responseDto);

    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(
            MethodArgumentNotValidException ex) {


        Map<String,String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(
                error-> fieldErrors.put(
                        error.getField(),error.getDefaultMessage()
                )
        );
        ErrorResponseDto responseDto = new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                Instant.now(),
                fieldErrors
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);

    }



    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDto> handleHttpMessageNotReadableException(
            MethodArgumentNotValidException ex) {


        Map<String,String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(
                error-> fieldErrors.put(
                        error.getField(),error.getDefaultMessage()
                )
        );
        ErrorResponseDto responseDto = new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid request body",
                Instant.now(),
                fieldErrors
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);

    }



}
