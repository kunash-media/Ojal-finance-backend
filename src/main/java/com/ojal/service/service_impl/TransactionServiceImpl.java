package com.ojal.service.service_impl;

import com.ojal.global_exception.ResourceNotFoundException;
import com.ojal.model_entity.SavingAccountsEntity;
import com.ojal.model_entity.TransactionEntity;
import com.ojal.model_entity.dto.request.TransactionDto;
import com.ojal.repository.SavingAccountsRepository;
import com.ojal.repository.TransactionRepository;
import com.ojal.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final SavingAccountsRepository savingAccountsRepository;

    @Autowired
    public TransactionServiceImpl(
            TransactionRepository transactionRepository,
            SavingAccountsRepository savingAccountsRepository) {
        this.transactionRepository = transactionRepository;
        this.savingAccountsRepository = savingAccountsRepository;
    }

    @Override
    @Transactional
    public TransactionEntity createTransaction(String accountNumber, TransactionDto transactionDto) {
        // Find the savings account
        SavingAccountsEntity savingAccount = savingAccountsRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Saving Account", "accountNumber", accountNumber));

        // Create and populate the transaction entity
        TransactionEntity transaction = new TransactionEntity();
        transaction.setSavingAccount(savingAccount);
        transaction.setDate(transactionDto.getDate() != null ? transactionDto.getDate() : LocalDateTime.now());
        transaction.setAmount(transactionDto.getAmount());
        transaction.setPayMode(transactionDto.getPayMode());
        transaction.setUtrNo(transactionDto.getUtrNo());
        transaction.setCash(transactionDto.getCash());
        transaction.setChequeNumber(transactionDto.getChequeNumber());
        transaction.setNote(transactionDto.getNote());

        // Update account balance based on transaction
        BigDecimal newBalance;
        if (transactionDto.getPayMode() != null &&
                (transactionDto.getPayMode().equalsIgnoreCase("WITHDRAWAL") ||
                        transactionDto.getPayMode().equalsIgnoreCase("DEBIT"))) {
            // For withdrawal/debit, subtract from balance
            newBalance = savingAccount.getBalance().subtract(transactionDto.getAmount());
        } else {
            // For deposit/credit, add to balance
            newBalance = savingAccount.getBalance().add(transactionDto.getAmount());
        }

        // Update balance in account and set balance after transaction
        savingAccount.setBalance(newBalance);
        transaction.setBalanceAfter(newBalance);

        // Save the updated account
        savingAccountsRepository.save(savingAccount);

        // Save and return the transaction
        return transactionRepository.save(transaction);
    }

    @Override
    public List<TransactionEntity> findByAccountNumber(String accountNumber) {
        // Verify account exists
        savingAccountsRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Saving Account", "accountNumber", accountNumber));

        // Return transactions ordered by date (newest first)
        return transactionRepository.findBySavingAccount_AccountNumberOrderByDateDesc(accountNumber);
    }
}