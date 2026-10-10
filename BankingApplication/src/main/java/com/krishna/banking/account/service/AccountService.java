package com.krishna.banking.account.service;

import com.krishna.banking.account.dto.*;
import com.krishna.banking.account.entity.Account;
import com.krishna.banking.account.entity.AccountStatus;
import com.krishna.banking.account.repository.AccountRepository;
import com.krishna.banking.common.exceptions.*;
import com.krishna.banking.transaction.service.TransactionService;
import com.krishna.banking.user.entity.User;
import com.krishna.banking.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountNumberGenerator numberGenerator;
    private final TransactionService transactionService;
    private final EntityManager entityManager;

    public AccountService(
            AccountRepository accountRepository,
            UserRepository userRepository,
            AccountNumberGenerator numberGenerator,
            TransactionService transactionService,
            EntityManager entityManager) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.numberGenerator = numberGenerator;
        this.transactionService = transactionService;
        this.entityManager = entityManager;
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
    public void closeAccount(Long userId, String accountNumber) {

        Account account =
                accountRepository.findLockedAccount(
                                accountNumber, userId
                        )
                        .orElseThrow(() ->
                                new AccountNotFoundException("Account not found"));

        if (account.getAccountStatus() == AccountStatus.CLOSED) {
            throw new AccountAlreadyClosedException(
                    "Account already closed"
            );
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


    //Account + Transaction service methods

    @Transactional
    public void deposit(Long userId,
                        DepositRequestDto requestDto,
                        String accountNumber){

        Account account =
                accountRepository.findLockedAccount(
                        accountNumber,userId)
                        .orElseThrow(()->
                                new AccountNotFoundException("Account not found"));

        if(account.getAccountStatus() == AccountStatus.CLOSED){
            throw new AccountAlreadyClosedException("Account already closed");
        }

        account.setBalance
                (account.getBalance()
                        .add(requestDto.amount()));


        transactionService.createDepositTransaction(account,
                requestDto.amount(),
                requestDto.paymentMethod());


    }


    @Transactional
    public void withdraw(Long userId,
                         WithdrawalRequestDto requestDto,
                         String accountNumber) {
        Account account =
                accountRepository.findLockedAccount(
                        accountNumber,userId)
                        .orElseThrow(()->
                                new AccountNotFoundException("Account not found"));


        if(account.getAccountStatus() == AccountStatus.CLOSED) {
            throw new AccountAlreadyClosedException("Account already closed");
        }

        if(account.getBalance().compareTo(requestDto.amount()) < 0){
            throw new InsufficientFundsException("Funds not sufficient");
        }

        account.setBalance(account.getBalance().subtract(requestDto.amount()));

        transactionService.createWithdrawTransaction(account,
                requestDto.amount(),
                requestDto.paymentMethod());


    }

    @Transactional
    public void transfer(
            Long userId,
            TransferRequestDto requestDto,
            String sourceAccountNumber) {

        // 1. Identify source
        Account sourceAccount =
                accountRepository.findByAccountNumberAndUserId(
                                sourceAccountNumber,
                                userId
                        )
                        .orElseThrow(() ->
                                new AccountNotFoundException("Account not found"));

        // 2. Identify destination
        Account destinationAccount =
                accountRepository.findByAccountNumber(
                                requestDto.destinationAccountNumber()
                        )
                        .orElseThrow(() ->
                                new AccountNotFoundException("Account not found"));

        // 3. Get IDs
        Long sourceId = sourceAccount.getId();
        Long destinationId = destinationAccount.getId();

        // 4. Same account check
        if (sourceId.equals(destinationId)) {
            throw new SameAccountTransferException(
                    "Cannot transfer money in same account"
            );
        }

        // 5. Determine LOCK ORDER only
        Account firstAccount;
        Account secondAccount;

        if (sourceId < destinationId) {
            firstAccount = sourceAccount;
            secondAccount = destinationAccount;
        } else {
            firstAccount = destinationAccount;
            secondAccount = sourceAccount;
        }

        // 6. Lock + refresh in consistent order
        entityManager.refresh(
                firstAccount,
                LockModeType.PESSIMISTIC_WRITE
        );

        entityManager.refresh(
                secondAccount,
                LockModeType.PESSIMISTIC_WRITE
        );

        // 7. Validate SOURCE
        if (sourceAccount.getAccountStatus() == AccountStatus.CLOSED) {
            throw new AccountAlreadyClosedException(
                    "Account already closed"
            );
        }

        // 8. Validate DESTINATION
        if (destinationAccount.getAccountStatus() == AccountStatus.CLOSED) {
            throw new AccountAlreadyClosedException(
                    "Account already closed"
            );
        }

        // 9. Check SOURCE balance
        if (sourceAccount.getBalance()
                .compareTo(requestDto.amount()) < 0) {

            throw new InsufficientFundsException(
                    "Funds not sufficient"
            );
        }

        // 10. Move money
        sourceAccount.setBalance(
                sourceAccount.getBalance()
                        .subtract(requestDto.amount())
        );

        destinationAccount.setBalance(
                destinationAccount.getBalance()
                        .add(requestDto.amount())
        );

        // 11. Record transaction
        transactionService.createTransferTransaction(
                sourceAccount,
                destinationAccount,
                requestDto.amount(),
                requestDto.paymentMethod()
        );
    }
}
