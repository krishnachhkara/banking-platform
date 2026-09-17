package com.krishna.banking.account.controller;

import com.krishna.banking.account.dto.AccountResponseDto;
import com.krishna.banking.account.dto.CreateAccountRequestDto;
import com.krishna.banking.account.service.AccountService;
import com.krishna.banking.user.security.CurrentUser;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;
    private final CurrentUser currentUser;

    public AccountController(AccountService accountService,
                             CurrentUser currentUser) {
        this.accountService = accountService;
        this.currentUser = currentUser;
    }


    @PostMapping
    public ResponseEntity<AccountResponseDto> createAccount(
            @Valid @RequestBody CreateAccountRequestDto requestDto){
        Long userId = currentUser.getId();

        AccountResponseDto responseDto =
                accountService.createAccount(userId,requestDto);

        return  ResponseEntity.status(HttpStatus.CREATED).body(responseDto);

    }

    @GetMapping
    public ResponseEntity<List<AccountResponseDto>> getAccounts(){
        Long userId = currentUser.getId();

        List<AccountResponseDto> responseDto =
                accountService.getAccounts(userId);

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountResponseDto> getAccount(
            @PathVariable String accountNumber
    ){
        Long userId = currentUser.getId();

        AccountResponseDto responseDto =
                accountService.getAccount(userId,accountNumber);

        return ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping("/{accountNumber}/close")
    public ResponseEntity<Void> closeAccount(
            @PathVariable String accountNumber
    ){
        Long userId = currentUser.getId();

        accountService.closeAccount(userId,accountNumber);

        return ResponseEntity.noContent().build();
    }
}

