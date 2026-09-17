package com.krishna.banking.account.service;

import com.krishna.banking.account.dto.AccountResponseDto;
import com.krishna.banking.account.dto.CreateAccountRequestDto;
import com.krishna.banking.account.entity.Account;
import com.krishna.banking.account.entity.AccountStatus;
import com.krishna.banking.account.repository.AccountRepository;
import com.krishna.banking.common.exceptions.AccountAlreadyClosedException;
import com.krishna.banking.common.exceptions.AccountAlreadyExistsException;
import com.krishna.banking.common.exceptions.AccountNotFoundException;
import com.krishna.banking.common.exceptions.UserNotFoundException;
import com.krishna.banking.user.entity.User;
import com.krishna.banking.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountNumberGenerator numberGenerator;

    public AccountService(
            AccountRepository accountRepository,
            UserRepository userRepository,
            AccountNumberGenerator numberGenerator) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.numberGenerator = numberGenerator;
    }


    @Transactional
    public AccountResponseDto createAccount(Long userId,
            CreateAccountRequestDto requestDto){

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException("User not found"));

        if(accountRepository.existsByUserIdAndAccountType(
                userId,requestDto.accountType())){
            throw new AccountAlreadyExistsException(
                    "User already has an account of this type"
            );
        }
        String accountNumber = numberGenerator.generate();

        Account newAccount = new Account();

        newAccount.setAccountNumber(accountNumber);
        newAccount.setAccountStatus(AccountStatus.ACTIVE);
        newAccount.setAccountType(requestDto.accountType());
        newAccount.setBalance(BigDecimal.ZERO);
        newAccount.setUser(user);
        // time createdAt and updatedAt are set by JPA auditing
        accountRepository.save(newAccount);

        return mapToDto(newAccount);

    }

    public List<AccountResponseDto> getAccounts(Long userId){
        List<Account> accounts = accountRepository.findByUserId(userId);

        return accounts.stream()
                .map(this::mapToDto)
                .toList();
    }

    public AccountResponseDto getAccount(Long userId,String accountNumber){
        Account account =
                accountRepository.findByAccountNumberAndUserId(accountNumber,userId)
                        .orElseThrow(()->
                                new AccountNotFoundException("Account not found"));

        return mapToDto(account);
    }

    @Transactional
    public void closeAccount(Long userId, String accountNumber){

        Account account =
                accountRepository.findByAccountNumberAndUserId(accountNumber,userId)
                        .orElseThrow(()->
                                new AccountNotFoundException("Account not found"));

        if(account.getAccountStatus() == AccountStatus.CLOSED){
            throw new AccountAlreadyClosedException("Account already closed");
        }

        account.setAccountStatus(AccountStatus.CLOSED);
    }



    private AccountResponseDto mapToDto(Account account){
        return new AccountResponseDto(
                account.getAccountNumber(),
                account.getAccountType(),
                account.getBalance(),
                account.getAccountStatus(),
                account.getCreatedAt()
        );
    }









}
