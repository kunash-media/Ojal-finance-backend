package com.ojal.service.service_impl;

import com.ojal.global_exception.ResourceNotFoundException;
import com.ojal.model_entity.RdAccountsEntity;
import com.ojal.model_entity.RdTransactionEntity;
import com.ojal.model_entity.dto.request.RdTransactionDTO;
import com.ojal.repository.RdAccountsRepository;
import com.ojal.repository.RdTransactionRepository;
import com.ojal.service.RdTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class RdTransactionServiceImpl implements RdTransactionService {

    private RdTransactionRepository rdTransactionRepository;
    private RdAccountsRepository rdAccountsRepository;

    @Autowired
    public RdTransactionServiceImpl(
            RdAccountsRepository rdAccountsRepository,
            RdTransactionRepository rdTransactionRepository) {
        this.rdAccountsRepository = rdAccountsRepository;
        this.rdTransactionRepository = rdTransactionRepository;
    }

    @Override
    @Transactional
    public RdTransactionEntity createTransaction(RdTransactionDTO transactionDTO) {
        // Find the RD account by account number
        RdAccountsEntity rdAccount = rdAccountsRepository.findByAccountNumber(transactionDTO.getAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("RD Account", "accountNumber", transactionDTO.getAccountNumber()));

        // Prepare timestamp
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
        String formattedTime = now.format(formatter);

        // Create and populate transaction entity
        RdTransactionEntity transaction = new RdTransactionEntity();

        // Set basic transaction details
        transaction.setRdAccount(rdAccount);
        transaction.setCreatedAt(formattedTime);
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setPayMode(transactionDTO.getPayMode());
        transaction.setUtrNo(transactionDTO.getUtrNo());
        transaction.setCash(transactionDTO.getCash());
        transaction.setChequeNumber(transactionDTO.getChequeNumber());
        transaction.setNote(transactionDTO.getNote());

        // Update account balance based on transaction

        BigDecimal newBalance;
        if (transactionDTO.getPayMode() != null &&
                (transactionDTO.getPayMode().equalsIgnoreCase("WITHDRAWAL") ||
                        transactionDTO.getPayMode().equalsIgnoreCase("DEBIT"))) {
            // For withdrawal/debit, subtract from balance
            newBalance = rdAccount.getBalance().subtract(transactionDTO.getAmount());
        } else {
            // For deposit/credit, add to balance
            newBalance = rdAccount.getBalance().add(transactionDTO.getAmount());
        }

        // Update balance in account and set balance after transaction
        rdAccount.setBalance(newBalance);
        transaction.setBalanceAfter(newBalance);

        // Set status or use default if not provided
        transaction.setStatus(transactionDTO.getStatus() != null
                ? transactionDTO.getStatus()
                : RdTransactionEntity.TransactionStatus.COMPLETED);

        // Save the updated account
        rdAccountsRepository.save(rdAccount);

        // Save and return the transaction
        return rdTransactionRepository.save(transaction);
    }

    @Override
    public List<RdTransactionEntity> getTransactionsByAccountNumber(String accountNumber) {
        // Verify the account exists
        if (!rdAccountsRepository.existsByAccountNumber(accountNumber)) {
            throw new NoSuchElementException("RD Account not found with account number: " + accountNumber);
        }

        return rdTransactionRepository.findByRdAccount_AccountNumber(accountNumber);
    }

    @Override
    public RdTransactionEntity getTransactionById(Long id) {
        return rdTransactionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Transaction not found with id: " + id));
    }

    @Override
    @Transactional
    public RdTransactionEntity updateTransactionStatus(Long id, RdTransactionEntity.TransactionStatus status) {
        RdTransactionEntity transaction = rdTransactionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Transaction not found with id: " + id));

        transaction.setStatus(status);
        return rdTransactionRepository.save(transaction);
    }

    @Override
    @Transactional
    public void deleteTransaction(Long id) {
        if (!rdTransactionRepository.existsById(id)) {
            throw new NoSuchElementException("Transaction not found with id: " + id);
        }
        rdTransactionRepository.deleteById(id);
    }
}