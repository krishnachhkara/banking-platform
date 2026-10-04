package com.krishna.banking.transaction.service;


import com.krishna.banking.account.entity.Account;
import com.krishna.banking.transaction.entity.PaymentMethod;
import com.krishna.banking.transaction.entity.Transaction;
import com.krishna.banking.transaction.entity.TransactionStatus;
import com.krishna.banking.transaction.entity.TransactionType;
import com.krishna.banking.transaction.repository.TransactionRepository;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
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
}
