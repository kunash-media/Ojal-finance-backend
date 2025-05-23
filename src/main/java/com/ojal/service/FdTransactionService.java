package com.ojal.service;

import com.ojal.model_entity.FdAccountsEntity;
import com.ojal.model_entity.FdTransactionEntity;
import com.ojal.model_entity.dto.request.FdTransactionDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for FD Transaction operations
 * Defines all business logic methods for FD transaction management
 */
public interface FdTransactionService {

    /**
     * Create a new FD transaction
     * @param transaction FD transaction entity to create
     * @return Created transaction entity
     */
    FdTransactionEntity createTransaction(FdTransactionDTO transactionDTO);

    /**
     * Get transaction by ID
     * @param id Transaction ID
     * @return Optional containing transaction if found
     */
    Optional<FdTransactionEntity> getTransactionById(Long id);

    /**
     * Get transaction by transaction ID
     * @param transactionId Unique transaction identifier
     * @return Optional containing transaction if found
     */
    Optional<FdTransactionEntity> getTransactionByTransactionId(String transactionId);

    /**
     * Get all transactions for a specific FD account
     * @param fdAccount FD account entity
     * @return List of transactions for the account
     */
    List<FdTransactionEntity> getTransactionsByFdAccount(FdAccountsEntity fdAccount);

    /**
     * Get all transactions for a specific FD account number
     * @param accountNumber FD account number
     * @return List of transactions for the account number
     */
    List<FdTransactionEntity> getTransactionsByAccountNumber(String accountNumber);

    /**
     * Get all transactions with specific status
     * @param status Transaction status
     * @return List of transactions with specified status
     */
    List<FdTransactionEntity> getTransactionsByStatus(FdTransactionEntity.TransactionStatus status);

    /**
     * Get all transactions with specific pay mode
     * @param payMode Payment mode
     * @return List of transactions with specified pay mode
     */
    List<FdTransactionEntity> getTransactionsByPayMode(FdTransactionEntity.PayMode payMode);

    /**
     * Get all transactions
     * @return List of all transactions
     */
    List<FdTransactionEntity> getAllTransactions();

    /**
     * Update an existing transaction
     * @param id Transaction ID to update
     * @param updatedTransaction Updated transaction data
     * @return Updated transaction entity
     */
    FdTransactionEntity updateTransaction(Long id, FdTransactionEntity updatedTransaction);

    /**
     * Update transaction status
     * @param transactionId Unique transaction identifier
     * @param status New transaction status
     * @return Updated transaction entity
     */
    FdTransactionEntity updateTransactionStatus(String transactionId, FdTransactionEntity.TransactionStatus status);

    /**
     * Delete transaction by ID
     * @param id Transaction ID to delete
     */
    void deleteTransaction(Long id);

    /**
     * Delete transaction by transaction ID
     * @param transactionId Unique transaction identifier
     */
    void deleteTransactionByTransactionId(String transactionId);

    /**
     * Delete all transactions for a specific FD account
     * @param fdAccount FD account entity
     */
    void deleteTransactionsByFdAccount(FdAccountsEntity fdAccount);

    /**
     * Get transaction count for a specific FD account
     * @param fdAccount FD account entity
     * @return Total number of transactions
     */
    long getTransactionCountByFdAccount(FdAccountsEntity fdAccount);

    /**
     * Check if transaction exists by transaction ID
     * @param transactionId Unique transaction identifier
     * @return true if transaction exists, false otherwise
     */
    boolean existsByTransactionId(String transactionId);
}