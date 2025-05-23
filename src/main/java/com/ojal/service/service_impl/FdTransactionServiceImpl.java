package com.ojal.service.service_impl;

import com.ojal.global_exception.ResourceNotFoundException;
import com.ojal.model_entity.FdAccountsEntity;
import com.ojal.model_entity.FdTransactionEntity;
import com.ojal.model_entity.dto.request.FdTransactionDTO;
import com.ojal.repository.FdAccountsRepository;
import com.ojal.repository.FdTransactionRepository;
import com.ojal.service.FdTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FdTransactionServiceImpl implements FdTransactionService {

    private final FdTransactionRepository fdTransactionRepository;
    private final FdAccountsRepository fdAccountsRepository;

    @Autowired
    public FdTransactionServiceImpl(FdTransactionRepository fdTransactionRepository, FdAccountsRepository fdAccountsRepository) {
        this.fdTransactionRepository = fdTransactionRepository;
        this.fdAccountsRepository = fdAccountsRepository;
    }

    @Override
    @Transactional
    public FdTransactionEntity createTransaction(FdTransactionDTO transactionDTO) {
        // Find the FD account by account number
        FdAccountsEntity fdAccount = fdAccountsRepository.findByAccountNumber(transactionDTO.getAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("FD Account", "accountNumber", transactionDTO.getAccountNumber()));

        // Create and populate transaction entity
        FdTransactionEntity transaction = new FdTransactionEntity();

        // Set basic transaction details
        transaction.setFdAccount(fdAccount);
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setPayMode(transactionDTO.getPayMode());
        transaction.setNote(transactionDTO.getNote());

        // Update account balance based on transaction
        BigDecimal newBalance;
        if (transactionDTO.getPayMode() != null &&
                (transactionDTO.getPayMode().equalsIgnoreCase("WITHDRAWAL") ||
                        transactionDTO.getPayMode().equalsIgnoreCase("DEBIT"))) {
            // For withdrawal, subtract from balance
            newBalance = fdAccount.getBalance().subtract(transactionDTO.getAmount());
        } else {
            // For deposit/credit, add to balance
            newBalance = fdAccount.getBalance().add(transactionDTO.getAmount());
        }

        // Update balance in account and set balance after transaction
        fdAccount.setBalance(newBalance);
        transaction.setBalanceAfter(newBalance);

        // Set status or use default if not provided
        transaction.setStatus(transactionDTO.getStatus() != null
                ? transactionDTO.getStatus()
                : FdTransactionEntity.TransactionStatus.SUCCESS);

        // Save the updated account
        fdAccountsRepository.save(fdAccount);

        // Save and return the transaction
        return fdTransactionRepository.save(transaction);
    }

    /**
     * Get transaction by ID
     * @param id Transaction ID
     * @return Optional containing transaction if found
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FdTransactionEntity> getTransactionById(Long id) {
        return fdTransactionRepository.findById(id);
    }

    /**
     * Get transaction by transaction ID
     * @param transactionId Unique transaction identifier
     * @return Optional containing transaction if found
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FdTransactionEntity> getTransactionByTransactionId(String transactionId) {
        return fdTransactionRepository.findByTransactionId(transactionId);
    }

    /**
     * Get all transactions for a specific FD account
     * @param fdAccount FD account entity
     * @return List of transactions for the account ordered by latest first
     */
    @Override
    @Transactional(readOnly = true)
    public List<FdTransactionEntity> getTransactionsByFdAccount(FdAccountsEntity fdAccount) {
        return fdTransactionRepository.findByFdAccountOrderByCreatedAtDesc(fdAccount);
    }

    /**
     * Get all transactions for a specific FD account number
     * @param accountNumber FD account number
     * @return List of transactions for the account number
     */
    @Override
    @Transactional(readOnly = true)
    public List<FdTransactionEntity> getTransactionsByAccountNumber(String accountNumber) {
        return fdTransactionRepository.findByAccountNumber(accountNumber);
    }

    /**
     * Get all transactions with specific status
     * @param status Transaction status
     * @return List of transactions with specified status
     */
    @Override
    @Transactional(readOnly = true)
    public List<FdTransactionEntity> getTransactionsByStatus(FdTransactionEntity.TransactionStatus status) {
        return fdTransactionRepository.findByStatus(status);
    }

    /**
     * Get all transactions with specific pay mode
     * @param payMode Payment mode
     * @return List of transactions with specified pay mode
     */
    @Override
    @Transactional(readOnly = true)
    public List<FdTransactionEntity> getTransactionsByPayMode(FdTransactionEntity.PayMode payMode) {
        return fdTransactionRepository.findByPayMode(payMode);
    }

    /**
     * Get all transactions
     * @return List of all transactions
     */
    @Override
    @Transactional(readOnly = true)
    public List<FdTransactionEntity> getAllTransactions() {
        return fdTransactionRepository.findAll();
    }

    /**
     * Update an existing transaction
     * @param id Transaction ID to update
     * @param updatedTransaction Updated transaction data
     * @return Updated transaction entity
     * @throws RuntimeException if transaction not found
     */
    @Override
    public FdTransactionEntity updateTransaction(Long id, FdTransactionEntity updatedTransaction) {
        Optional<FdTransactionEntity> existingTransaction = fdTransactionRepository.findById(id);

        if (existingTransaction.isPresent()) {
            FdTransactionEntity transaction = existingTransaction.get();

            // Update modifiable fields only
            if (updatedTransaction.getAmount() != null) {
                transaction.setAmount(updatedTransaction.getAmount());
            }
            if (updatedTransaction.getPayMode() != null) {
                transaction.setPayMode(updatedTransaction.getPayMode());
            }
            if (updatedTransaction.getStatus() != null) {
                transaction.setStatus(updatedTransaction.getStatus());
            }
            if (updatedTransaction.getNote() != null) {
                transaction.setNote(updatedTransaction.getNote());
            }
            if (updatedTransaction.getBalanceAfter() != null) {
                transaction.setBalanceAfter(updatedTransaction.getBalanceAfter());
            }

            return fdTransactionRepository.save(transaction);
        } else {
            throw new RuntimeException("Transaction not found with ID: " + id);
        }
    }

    /**
     * Update transaction status
     * @param transactionId Unique transaction identifier
     * @param status New transaction status
     * @return Updated transaction entity
     * @throws RuntimeException if transaction not found
     */
    @Override
    public FdTransactionEntity updateTransactionStatus(String transactionId, FdTransactionEntity.TransactionStatus status) {
        Optional<FdTransactionEntity> existingTransaction = fdTransactionRepository.findByTransactionId(transactionId);

        if (existingTransaction.isPresent()) {
            FdTransactionEntity transaction = existingTransaction.get();
            transaction.setStatus(status);
            return fdTransactionRepository.save(transaction);
        } else {
            throw new RuntimeException("Transaction not found with Transaction ID: " + transactionId);
        }
    }

    /**
     * Delete transaction by ID
     * @param id Transaction ID to delete
     * @throws RuntimeException if transaction not found
     */
    @Override
    public void deleteTransaction(Long id) {
        if (fdTransactionRepository.existsById(id)) {
            fdTransactionRepository.deleteById(id);
        } else {
            throw new RuntimeException("Transaction not found with ID: " + id);
        }
    }

    /**
     * Delete transaction by transaction ID
     * @param transactionId Unique transaction identifier
     * @throws RuntimeException if transaction not found
     */
    @Override
    public void deleteTransactionByTransactionId(String transactionId) {
        Optional<FdTransactionEntity> transaction = fdTransactionRepository.findByTransactionId(transactionId);

        if (transaction.isPresent()) {
            fdTransactionRepository.delete(transaction.get());
        } else {
            throw new RuntimeException("Transaction not found with Transaction ID: " + transactionId);
        }
    }

    /**
     * Delete all transactions for a specific FD account
     * @param fdAccount FD account entity
     */
    @Override
    public void deleteTransactionsByFdAccount(FdAccountsEntity fdAccount) {
        fdTransactionRepository.deleteByFdAccount(fdAccount);
    }

    /**
     * Get transaction count for a specific FD account
     * @param fdAccount FD account entity
     * @return Total number of transactions
     */
    @Override
    @Transactional(readOnly = true)
    public long getTransactionCountByFdAccount(FdAccountsEntity fdAccount) {
        return fdTransactionRepository.countByFdAccount(fdAccount);
    }

    /**
     * Check if transaction exists by transaction ID
     * @param transactionId Unique transaction identifier
     * @return true if transaction exists, false otherwise
     */
    @Override
    @Transactional(readOnly = true)
    public boolean existsByTransactionId(String transactionId) {
        return fdTransactionRepository.existsByTransactionId(transactionId);
    }

    /**
     * Validate transaction data before processing
     * @param transaction Transaction entity to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateTransaction(FdTransactionEntity transaction) {
        if (transaction.getFdAccount() == null) {
            throw new IllegalArgumentException("FD Account cannot be null");
        }
        if (transaction.getAmount() == null || transaction.getAmount().signum() <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (transaction.getPayMode() == null) {
            throw new IllegalArgumentException("Pay mode cannot be null");
        }
        if (transaction.getBalanceAfter() == null || transaction.getBalanceAfter().signum() < 0) {
            throw new IllegalArgumentException("Balance after cannot be negative");
        }
    }
}