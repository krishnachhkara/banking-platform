package com.krishna.banking.account.controller;

import com.krishna.banking.account.dto.*;
import com.krishna.banking.account.service.AccountService;
import com.krishna.banking.common.exceptions.InvalidPaginationParameterException;
import com.krishna.banking.transaction.dto.TransactionResponseDto;
import com.krishna.banking.transaction.service.TransactionService;
import com.krishna.banking.user.security.CurrentUser;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;
    private final CurrentUser currentUser;
    private  final TransactionService transactionService;

    public AccountController(AccountService accountService,
                             CurrentUser currentUser,
                             TransactionService transactionService) {
        this.accountService = accountService;
        this.currentUser = currentUser;
        this.transactionService = transactionService;
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

    //Transactions endpoints

    @PostMapping("/{accountNumber}/deposit")
    public ResponseEntity<SuccessResponseDto> deposit(
            @PathVariable String accountNumber,
            @Valid @RequestBody DepositRequestDto requestDto
    ){

        Long userId = currentUser.getId();

        accountService.deposit(userId,requestDto,accountNumber);

        return ResponseEntity.ok(new SuccessResponseDto("Deposit successful"));

    }

    @PostMapping("/{accountNumber}/withdraw")
    public ResponseEntity<SuccessResponseDto> withdraw(
            @PathVariable String accountNumber,
            @Valid @RequestBody WithdrawalRequestDto requestDto
    ){

        Long userId = currentUser.getId();

        accountService.withdraw(userId,requestDto,accountNumber);

        return ResponseEntity.ok(new SuccessResponseDto("Withdrawal successful"));

    }

    @PostMapping("/{sourceAccountNumber}/transfer")
    public ResponseEntity<SuccessResponseDto> transfer(
            @PathVariable String sourceAccountNumber,
            @Valid @RequestBody TransferRequestDto requestDto
    ){

        Long userId = currentUser.getId();

        accountService.transfer(userId,requestDto,sourceAccountNumber);

        return ResponseEntity.ok(new SuccessResponseDto("Transfer successful"));

    }

    @GetMapping("/{accountNumber}/transactions")
    public ResponseEntity<Page<TransactionResponseDto>> transactionHistory(
            @PathVariable String accountNumber,

            @RequestParam (name = "page",required = false)
            Integer requestedPage,

            @RequestParam (name = "size", required = false)
            Integer requestedSize,

            @PageableDefault(
                    size = 10,
                    sort = {"createdAt","id"},
                    direction = Sort.Direction.DESC)
            Pageable pageable){

        Long userId = currentUser.getId();

        if(requestedPage != null && requestedPage < 0){
            throw new InvalidPaginationParameterException(
                    "Page must be greater than or equal to 0"
            );
        }

        if(requestedSize != null && (requestedSize < 1 || requestedSize > 100)){
            throw new InvalidPaginationParameterException(
                    "Page size must be between 1 and 100"
            );
        }

        Page<TransactionResponseDto> transactions =
                transactionService.transactionHistory(userId,accountNumber,pageable);

        return ResponseEntity.ok(transactions);
    }
}

