package com.krishna.banking.transaction.repository;

import com.krishna.banking.transaction.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
        @EntityGraph(attributePaths = {
                "senderAccount",
                "receiverAccount"
        })
        Page<Transaction> findBySenderAccountIdOrReceiverAccountId(
                Long senderAccountId,
                Long receiverAccountId,
                Pageable pageable
        );
}
