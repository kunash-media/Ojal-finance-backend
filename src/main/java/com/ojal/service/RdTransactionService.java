package com.ojal.service;

import com.ojal.model_entity.RdTransactionEntity;
import com.ojal.model_entity.dto.request.RdTransactionDTO;

import java.util.List;

public interface RdTransactionService {

    // Create a new transaction
    RdTransactionEntity createTransaction(RdTransactionDTO transactionDTO);

    // Get all transactions for an RD account
    List<RdTransactionEntity> getTransactionsByAccountNumber(String accountNumber);

    // Get transaction by ID
    RdTransactionEntity getTransactionById(Long id);

    // Update transaction status
    RdTransactionEntity updateTransactionStatus(Long id, RdTransactionEntity.TransactionStatus status);

    // Delete transaction
    void deleteTransaction(Long id);
}