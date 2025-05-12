package com.ojal.repository;

import com.ojal.model_entity.SavingTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavingTransactionRepository extends JpaRepository<SavingTransactionEntity, Long> {
    List<SavingTransactionEntity> findBySavingAccount_AccountNumberOrderByCreatedAtDesc(String accountNumber);
}