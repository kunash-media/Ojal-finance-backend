package com.ojal.service.service_impl;

import com.ojal.global_exception.ResourceNotFoundException;
import com.ojal.model_entity.FdAccountsEntity;
import com.ojal.model_entity.FdTransactionEntity;
import com.ojal.model_entity.dto.request.FdTransactionDTO;
import com.ojal.repository.FdAccountsRepository;
import com.ojal.repository.FdTransactionRepository;
import com.ojal.service.FdTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FdTransactionServiceImpl implements FdTransactionService {

    private static final Logger logger = LoggerFactory.getLogger(FdTransactionServiceImpl.class);

    private final FdTransactionRepository fdTransactionRepository;
    private final FdAccountsRepository fdAccountsRepository;

    @Autowired
    public FdTransactionServiceImpl(FdTransactionRepository fdTransactionRepository, FdAccountsRepository fdAccountsRepository) {
        this.fdTransactionRepository = fdTransactionRepository;
        this.fdAccountsRepository = fdAccountsRepository;
        logger.debug("Initialized FdTransactionServiceImpl");
    }


    @Override
    @Transactional
    public FdTransactionEntity createTransaction(FdTransactionDTO transactionDTO) {
        logger.info("Starting createTransaction for accountNumber: {}, payMode: {}",
                transactionDTO.getAccountNumber(), transactionDTO.getPayMode());
        try {
            // Find the FD account by account number
            logger.debug("Fetching FD account for accountNumber: {}", transactionDTO.getAccountNumber());
            FdAccountsEntity fdAccount = fdAccountsRepository.findByAccountNumber(transactionDTO.getAccountNumber())
                    .orElseThrow(() -> {
                        logger.error("FD Account not found for accountNumber: {}", transactionDTO.getAccountNumber());
                        return new ResourceNotFoundException("FD Account", "accountNumber", transactionDTO.getAccountNumber());
                    });

            // Log initial balance and transaction amount for debugging
            logger.debug("Initial account balance: {}, Transaction amount: {}",
                    fdAccount.getBalance(), transactionDTO.getAmount());

            // Create and populate transaction entity
            FdTransactionEntity transaction = new FdTransactionEntity();
            logger.debug("Creating new transaction entity for accountNumber: {}", transactionDTO.getAccountNumber());

            // Set basic transaction details
            transaction.setFdAccount(fdAccount);
            // Ensure amount is stored as positive in DB
            BigDecimal transactionAmount = transactionDTO.getAmount().abs();
            transaction.setAmount(transactionAmount);
            transaction.setPayMode(transactionDTO.getPayMode());
            transaction.setNote(transactionDTO.getNote());
            transaction.setUtrNo(transactionDTO.getUtrNo());

            // Update account balance based on transaction
            BigDecimal newBalance;
            boolean isWithdrawal = transactionDTO.getPayMode() != null &&
                    (transactionDTO.getPayMode().equalsIgnoreCase("WITHDRAWAL") ||
                            transactionDTO.getPayMode().equalsIgnoreCase("DEBIT"));

            if (isWithdrawal) {
                // For withdrawal, check if sufficient balance exists
                logger.debug("Processing withdrawal: Current balance={}, Amount={}",
                        fdAccount.getBalance(), transactionAmount);
                if (fdAccount.getBalance().compareTo(transactionAmount) < 0) {
                    logger.error("Insufficient balance for withdrawal. Current: {}, Required: {}",
                            fdAccount.getBalance(), transactionAmount);
                    throw new IllegalArgumentException("Insufficient balance for withdrawal. Current balance: " +
                            fdAccount.getBalance() + ", Required: " + transactionAmount);
                }
                // For withdrawal, subtract from balance
                newBalance = fdAccount.getBalance().subtract(transactionAmount);
                logger.debug("Withdrawal transaction: New balance calculated as {}", newBalance);
            } else {
                // For deposit/credit, add to balance
                newBalance = fdAccount.getBalance().add(transactionAmount);
                logger.debug("Deposit transaction: New balance calculated as {}", newBalance);
            }

            // Update balance in account and set balance after transaction
            fdAccount.setBalance(newBalance);
            transaction.setBalanceAfter(newBalance);

            // Set status or use default if not provided
            transaction.setStatus(transactionDTO.getStatus() != null
                    ? transactionDTO.getStatus()
                    : FdTransactionEntity.TransactionStatus.SUCCESS);

            // Validate transaction before saving
            validateTransaction(transaction);
            logger.debug("Transaction validated successfully");

            // Save the updated account
            logger.debug("Saving updated FD account with new balance: {}", newBalance);
            fdAccountsRepository.save(fdAccount);

            // Save and return the transaction
            logger.debug("Saving transaction for accountNumber: {}", transactionDTO.getAccountNumber());
            FdTransactionEntity savedTransaction = fdTransactionRepository.save(transaction);
            logger.info("Successfully created transaction with ID: {}", savedTransaction.getId());
            return savedTransaction;
        } catch (IllegalArgumentException e) {
            logger.error("Validation error creating transaction for accountNumber: {}, payMode: {}",
                    transactionDTO.getAccountNumber(), transactionDTO.getPayMode(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Error creating transaction for accountNumber: {}, payMode: {}",
                    transactionDTO.getAccountNumber(), transactionDTO.getPayMode(), e);
            throw e;
        }
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<FdTransactionEntity> getTransactionById(Long id) {
        logger.info("Fetching transaction by ID: {}", id);
        try {
            Optional<FdTransactionEntity> transaction = fdTransactionRepository.findById(id);
            if (transaction.isPresent()) {
                logger.debug("Transaction found with ID: {}", id);
            } else {
                logger.warn("Transaction not found with ID: {}", id);
            }
            return transaction;
        } catch (Exception e) {
            logger.error("Error fetching transaction by ID: {}", id, e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FdTransactionEntity> getTransactionByTransactionId(String transactionId) {
        logger.info("Fetching transaction by transactionId: {}", transactionId);
        try {
            Optional<FdTransactionEntity> transaction = fdTransactionRepository.findByTransactionId(transactionId);
            if (transaction.isPresent()) {
                logger.debug("Transaction found with transactionId: {}", transactionId);
            } else {
                logger.warn("Transaction not found with transactionId: {}", transactionId);
            }
            return transaction;
        } catch (Exception e) {
            logger.error("Error fetching transaction by transactionId: {}", transactionId, e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<FdTransactionEntity> getTransactionsByFdAccount(FdAccountsEntity fdAccount) {
        logger.info("Fetching transactions for FD account: {}", fdAccount.getId());
        try {
            List<FdTransactionEntity> transactions = fdTransactionRepository.findByFdAccountOrderByCreatedAtDesc(fdAccount);
            logger.debug("Found {} transactions for FD account: {}", transactions.size(), fdAccount.getId());
            return transactions;
        } catch (Exception e) {
            logger.error("Error fetching transactions for FD account: {}", fdAccount.getId(), e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<FdTransactionEntity> getTransactionsByAccountNumber(String accountNumber) {
        logger.info("Fetching transactions for accountNumber: {}", accountNumber);
        try {
            List<FdTransactionEntity> transactions = fdTransactionRepository.findByAccountNumber(accountNumber);
            logger.debug("Found {} transactions for accountNumber: {}", transactions.size(), accountNumber);
            return transactions;
        } catch (Exception e) {
            logger.error("Error fetching transactions for accountNumber: {}", accountNumber, e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<FdTransactionEntity> getTransactionsByStatus(FdTransactionEntity.TransactionStatus status) {
        logger.info("Fetching transactions with status: {}", status);
        try {
            List<FdTransactionEntity> transactions = fdTransactionRepository.findByStatus(status);
            logger.debug("Found {} transactions with status: {}", transactions.size(), status);
            return transactions;
        } catch (Exception e) {
            logger.error("Error fetching transactions with status: {}", status, e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<FdTransactionEntity> getTransactionsByPayMode(FdTransactionEntity.PayMode payMode) {
        logger.info("Fetching transactions with payMode: {}", payMode);
        try {
            List<FdTransactionEntity> transactions = fdTransactionRepository.findByPayMode(payMode);
            logger.debug("Found {} transactions with payMode: {}", transactions.size(), payMode);
            return transactions;
        } catch (Exception e) {
            logger.error("Error fetching transactions with payMode: {}", payMode, e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<FdTransactionEntity> getAllTransactions() {
        logger.info("Fetching all transactions");
        try {
            List<FdTransactionEntity> transactions = fdTransactionRepository.findAll();
            logger.debug("Found {} transactions in total", transactions.size());
            return transactions;
        } catch (Exception e) {
            logger.error("Error fetching all transactions", e);
            throw e;
        }
    }

    @Override
    public FdTransactionEntity updateTransaction(Long id, FdTransactionEntity updatedTransaction) {
        logger.info("Updating transaction with ID: {}", id);
        try {
            Optional<FdTransactionEntity> existingTransaction = fdTransactionRepository.findById(id);

            if (existingTransaction.isPresent()) {
                FdTransactionEntity transaction = existingTransaction.get();

                // Update modifiable fields only
                if (updatedTransaction.getAmount() != null) {
                    transaction.setAmount(updatedTransaction.getAmount().abs());
                    logger.debug("Updated amount to: {}", transaction.getAmount());
                }
                if (updatedTransaction.getPayMode() != null) {
                    transaction.setPayMode(updatedTransaction.getPayMode());
                    logger.debug("Updated payMode to: {}", transaction.getPayMode());
                }
                if (updatedTransaction.getStatus() != null) {
                    transaction.setStatus(updatedTransaction.getStatus());
                    logger.debug("Updated status to: {}", transaction.getStatus());
                }
                if (updatedTransaction.getNote() != null) {
                    transaction.setNote(updatedTransaction.getNote());
                    logger.debug("Updated note to: {}", transaction.getNote());
                }
                if (updatedTransaction.getBalanceAfter() != null) {
                    transaction.setBalanceAfter(updatedTransaction.getBalanceAfter());
                    logger.debug("Updated balanceAfter to: {}", transaction.getBalanceAfter());
                }

                logger.debug("Saving updated transaction with ID: {}", id);
                FdTransactionEntity savedTransaction = fdTransactionRepository.save(transaction);
                logger.info("Successfully updated transaction with ID: {}", id);
                return savedTransaction;
            } else {
                logger.error("Transaction not found with ID: {}", id);
                throw new RuntimeException("Transaction not found with ID: " + id);
            }
        } catch (Exception e) {
            logger.error("Error updating transaction with ID: {}", id, e);
            throw e;
        }
    }

    @Override
    public FdTransactionEntity updateTransactionStatus(String transactionId, FdTransactionEntity.TransactionStatus status) {
        logger.info("Updating transaction status for transactionId: {}", transactionId);
        try {
            Optional<FdTransactionEntity> existingTransaction = fdTransactionRepository.findByTransactionId(transactionId);

            if (existingTransaction.isPresent()) {
                FdTransactionEntity transaction = existingTransaction.get();
                transaction.setStatus(status);
                logger.debug("Updated status to: {} for transactionId: {}", status, transactionId);
                FdTransactionEntity savedTransaction = fdTransactionRepository.save(transaction);
                logger.info("Successfully updated transaction status for transactionId: {}", transactionId);
                return savedTransaction;
            } else {
                logger.error("Transaction not found with transactionId: {}", transactionId);
                throw new RuntimeException("Transaction not found with Transaction ID: " + transactionId);
            }
        } catch (Exception e) {
            logger.error("Error updating transaction status for transactionId: {}", transactionId, e);
            throw e;
        }
    }

    @Override
    public void deleteTransaction(Long id) {
        logger.info("Deleting transaction with ID: {}", id);
        try {
            if (fdTransactionRepository.existsById(id)) {
                fdTransactionRepository.deleteById(id);
                logger.debug("Successfully deleted transaction with ID: {}", id);
            } else {
                logger.error("Transaction not found with ID: {}", id);
                throw new RuntimeException("Transaction not found with ID: " + id);
            }
        } catch (Exception e) {
            logger.error("Error deleting transaction with ID: {}", id, e);
            throw e;
        }
    }

    @Override
    public void deleteTransactionByTransactionId(String transactionId) {
        logger.info("Deleting transaction with transactionId: {}", transactionId);
        try {
            Optional<FdTransactionEntity> transaction = fdTransactionRepository.findByTransactionId(transactionId);

            if (transaction.isPresent()) {
                fdTransactionRepository.delete(transaction.get());
                logger.debug("Successfully deleted transaction with transactionId: {}", transactionId);
            } else {
                logger.error("Transaction not found with transactionId: {}", transactionId);
                throw new RuntimeException("Transaction not found with Transaction ID: " + transactionId);
            }
        } catch (Exception e) {
            logger.error("Error deleting transaction with transactionId: {}", transactionId, e);
            throw e;
        }
    }

    @Override
    public void deleteTransactionsByFdAccount(FdAccountsEntity fdAccount) {
        logger.info("Deleting transactions for FD account: {}", fdAccount.getId());
        try {
            fdTransactionRepository.deleteByFdAccount(fdAccount);
            logger.debug("Successfully deleted transactions for FD account: {}", fdAccount.getId());
        } catch (Exception e) {
            logger.error("Error deleting transactions for FD account: {}", fdAccount.getId(), e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public long getTransactionCountByFdAccount(FdAccountsEntity fdAccount) {
        logger.info("Fetching transaction count for FD account: {}", fdAccount.getId());
        try {
            long count = fdTransactionRepository.countByFdAccount(fdAccount);
            logger.debug("Transaction count for FD account {}: {}", fdAccount.getId(), count);
            return count;
        } catch (Exception e) {
            logger.error("Error fetching transaction count for FD account: {}", fdAccount.getId(), e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByTransactionId(String transactionId) {
        logger.info("Checking if transaction exists with transactionId: {}", transactionId);
        try {
            boolean exists = fdTransactionRepository.existsByTransactionId(transactionId);
            logger.debug("Transaction exists with transactionId: {} - {}", transactionId, exists);
            return exists;
        } catch (Exception e) {
            logger.error("Error checking transaction existence for transactionId: {}", transactionId, e);
            throw e;
        }
    }

    private void validateTransaction(FdTransactionEntity transaction) {
        logger.debug("Validating transaction for account: {}", transaction.getFdAccount().getId());
        try {
            if (transaction.getFdAccount() == null) {
                logger.error("Validation failed: FD Account cannot be null");
                throw new IllegalArgumentException("FD Account cannot be null");
            }
            if (transaction.getAmount() == null || transaction.getAmount().signum() <= 0) {
                logger.error("Validation failed: Amount must be positive");
                throw new IllegalArgumentException("Amount must be positive");
            }
            if (transaction.getPayMode() == null) {
                logger.error("Validation failed: Pay mode cannot be null");
                throw new IllegalArgumentException("Pay mode cannot be null");
            }
            if (transaction.getBalanceAfter() == null) {
                logger.error("Validation failed: Balance after cannot be null");
                throw new IllegalArgumentException("Balance after cannot be null");
            }
            boolean isWithdrawal = transaction.getPayMode() != null &&
                    (transaction.getPayMode().equalsIgnoreCase("WITHDRAWAL") ||
                            transaction.getPayMode().equalsIgnoreCase("DEBIT"));
            if (isWithdrawal && transaction.getBalanceAfter().signum() < 0) {
                logger.error("Validation failed: Balance after cannot be negative for withdrawal/debit");
                throw new IllegalArgumentException("Balance after cannot be negative for withdrawal/debit");
            }
            logger.debug("Transaction validation successful");
        } catch (Exception e) {
            logger.error("Error during transaction validation", e);
            throw e;
        }
    }
}