package com.krishna.banking.account.repository;

import com.krishna.banking.account.entity.Account;
import com.krishna.banking.account.entity.AccountType;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Long> {

    boolean existsByUserIdAndAccountType(Long userId, AccountType accountType);

    List<Account> findByUserId(Long userId);

    Optional<Account> findByAccountNumberAndUserId(String accountNumber,Long userId);

    Optional<Account> findByAccountNumber(String accountNumber);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    SELECT a
    FROM Account a
    WHERE a.accountNumber = :accountNumber
    AND a.user.id = :userId
""")
    Optional<Account> findLockedAccount(
            @Param("accountNumber") String accountNumber,
            @Param("userId") Long userId);



    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT a
        FROM Account a
        WHERE a.id = :id
""")
    Optional<Account> findLockedAccountById(@Param("id") Long id);
}
