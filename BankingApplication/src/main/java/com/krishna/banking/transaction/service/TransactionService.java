package com.krishna.banking.transaction.service;


import com.krishna.banking.account.entity.Account;
import com.krishna.banking.account.repository.AccountRepository;
import com.krishna.banking.common.exceptions.AccountNotFoundException;
import com.krishna.banking.transaction.dto.TransactionResponseDto;
import com.krishna.banking.transaction.entity.PaymentMethod;
import com.krishna.banking.transaction.entity.Transaction;
import com.krishna.banking.transaction.entity.TransactionStatus;
import com.krishna.banking.transaction.entity.TransactionType;
import com.krishna.banking.transaction.repository.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }


    public void createDepositTransaction(Account account,
                                         BigDecimal amount,
                                         PaymentMethod paymentMethod){

        Transaction transaction = new Transaction();

        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setAmount(amount);
        transaction.setPaymentMethod(paymentMethod);
        transaction.setSenderAccount(null);
        transaction.setReceiverAccount(account);
        transaction.setTransactionStatus(TransactionStatus.SUCCESS);

        transactionRepository.save(transaction);
    }


    public void createWithdrawTransaction(Account account,
                                          BigDecimal amount,
                                          PaymentMethod paymentMethod) {

        Transaction transaction = new Transaction();

        transaction.setTransactionType(TransactionType.WITHDRAWAL);
        transaction.setTransactionStatus(TransactionStatus.SUCCESS);
        transaction.setReceiverAccount(null);
        transaction.setSenderAccount(account);
        transaction.setPaymentMethod(paymentMethod);
        transaction.setAmount(amount);

        transactionRepository.save(transaction);

    }

    public void createTransferTransaction(Account sourceAccount,
                                          Account destinationAccount,
                                          BigDecimal amount,
                                          PaymentMethod paymentMethod) {

        Transaction transaction = new Transaction();

        transaction.setPaymentMethod(paymentMethod);
        transaction.setSenderAccount(sourceAccount);
        transaction.setReceiverAccount(destinationAccount);
        transaction.setTransactionStatus(TransactionStatus.SUCCESS);
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setAmount(amount);

        transactionRepository.save(transaction);
    }

    public Page<TransactionResponseDto> transactionHistory(
            Long userId, String accountNumber,
            Pageable pageable){
        Account account = accountRepository.findByAccountNumberAndUserId(
                accountNumber,userId)
                            .orElseThrow(()-> new AccountNotFoundException("Account not found"));

        return transactionRepository.findBySenderAccountIdOrReceiverAccountId(
                account.getId(),
                account.getId(),
                pageable
        ).map(this::toResponseDto);
    }



    private TransactionResponseDto toResponseDto(Transaction transaction) {
        return new TransactionResponseDto(
                transaction.getId(),
                transaction.getTransactionType(),
                transaction.getTransactionStatus(),
                transaction.getAmount(),
                transaction.getPaymentMethod(),
                transaction.getSenderAccount() == null
                        ? null
                        : transaction.getSenderAccount().getAccountNumber(),
                transaction.getReceiverAccount() == null
                        ? null
                        : transaction.getReceiverAccount().getAccountNumber(),
                transaction.getCreatedAt()
        );
    }

}
