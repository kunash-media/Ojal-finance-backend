package com.ojal.repository;

import com.ojal.model_entity.FdAccountsEntity;
import com.ojal.model_entity.FdTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for FD Transaction operations
 * Provides CRUD operations and custom queries for FD transactions
 */
@Repository
public interface FdTransactionRepository extends JpaRepository<FdTransactionEntity, Long> {

    /**
     * Find transaction by unique transaction ID
     * @param transactionId unique transaction identifier
     * @return Optional containing transaction if found
     */
    Optional<FdTransactionEntity> findByTransactionId(String transactionId);

    /**
     * Find all transactions for a specific FD account
     * @param fdAccount FD account entity
     * @return List of transactions for the account
     */
    List<FdTransactionEntity> findByFdAccount(FdAccountsEntity fdAccount);

    /**
     * Find all transactions for a specific FD account ordered by creation date descending
     * @param fdAccount FD account entity
     * @return List of transactions ordered by latest first
     */
    List<FdTransactionEntity> findByFdAccountOrderByCreatedAtDesc(FdAccountsEntity fdAccount);

    /**
     * Find transactions by FD account number using custom query
     * @param accountNumber FD account number
     * @return List of transactions for the account number
     */
    @Query("SELECT ft FROM FdTransactionEntity ft WHERE ft.fdAccount.accountNumber = :accountNumber ORDER BY ft.createdAt DESC")
    List<FdTransactionEntity> findByAccountNumber(@Param("accountNumber") String accountNumber);

    /**
     * Find transactions by status
     * @param status Transaction status
     * @return List of transactions with specified status
     */
    List<FdTransactionEntity> findByStatus(FdTransactionEntity.TransactionStatus status);

    /**
     * Find transactions by pay mode
     * @param payMode Payment mode
     * @return List of transactions with specified pay mode
     */
    List<FdTransactionEntity> findByPayMode(FdTransactionEntity.PayMode payMode);

    /**
     * Count total transactions for a specific FD account
     * @param fdAccount FD account entity
     * @return Total number of transactions
     */
    long countByFdAccount(FdAccountsEntity fdAccount);

    /**
     * Check if transaction exists by transaction ID
     * @param transactionId unique transaction identifier
     * @return true if transaction exists, false otherwise
     */
    boolean existsByTransactionId(String transactionId);

    /**
     * Delete transactions by FD account
     * @param fdAccount FD account entity
     */
    void deleteByFdAccount(FdAccountsEntity fdAccount);
}