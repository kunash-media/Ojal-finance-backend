package com.ojal.repository;



import com.ojal.model_entity.RdTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RdTransactionRepository extends JpaRepository<RdTransactionEntity, Long> {

    // Find all transactions for a specific RD account
    List<RdTransactionEntity> findByRdAccount_AccountNumber(String accountNumber);

    // Find transactions by status for a specific account
    List<RdTransactionEntity> findByRdAccount_AccountNumberAndStatus(String accountNumber, RdTransactionEntity.TransactionStatus status);
}