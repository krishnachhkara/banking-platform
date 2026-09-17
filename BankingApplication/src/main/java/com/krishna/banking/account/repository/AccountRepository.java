package com.krishna.banking.account.repository;

import com.krishna.banking.account.entity.Account;
import com.krishna.banking.account.entity.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Long> {

    boolean existsByUserIdAndAccountType(Long userId, AccountType accountType);

    List<Account> findByUserId(Long userId);

    Optional<Account> findByAccountNumberAndUserId(String accountNumber,Long userId);
}
