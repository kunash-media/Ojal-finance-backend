package com.ojal.service.service_impl;

import com.ojal.global_exception.ResourceNotFoundException;
import com.ojal.model_entity.SavingAccountsEntity;
import com.ojal.model_entity.SavingTransactionEntity;
import com.ojal.model_entity.dto.request.SavingTransactionDto;
import com.ojal.repository.SavingAccountsRepository;
import com.ojal.repository.SavingTransactionRepository;
import com.ojal.service.SavingTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class SavingTransactionServiceImpl implements SavingTransactionService {

    private final SavingTransactionRepository savingTransactionRepository;
    private final SavingAccountsRepository savingAccountsRepository;

    @Autowired
    public SavingTransactionServiceImpl(
            SavingTransactionRepository savingTransactionRepository,
            SavingAccountsRepository savingAccountsRepository) {
        this.savingTransactionRepository = savingTransactionRepository;
        this.savingAccountsRepository = savingAccountsRepository;
    }

    @Override
    @Transactional
    public SavingTransactionEntity createTransaction(String accountNumber, SavingTransactionDto savingTransactionDto) {

        // Find the savings account
        SavingAccountsEntity savingAccount = savingAccountsRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Saving Account", "accountNumber", accountNumber));

        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
        String formattedTime = now.format(formatter);

        SavingTransactionEntity transaction = new SavingTransactionEntity();

        transaction.setSavingAccount(savingAccount);

        transaction.setCreatedAt(formattedTime);

        transaction.setAmount(savingTransactionDto.getAmount());
        transaction.setPayMode(savingTransactionDto.getPayMode());
        transaction.setUtrNo(savingTransactionDto.getUtrNo());
        transaction.setCash(savingTransactionDto.getCash());
        transaction.setChequeNumber(savingTransactionDto.getChequeNumber());
        transaction.setNote(savingTransactionDto.getNote());

        // Update account balance based on transaction
        BigDecimal newBalance;
        if (savingTransactionDto.getPayMode() != null &&
                (savingTransactionDto.getPayMode().equalsIgnoreCase("WITHDRAWAL") ||
                        savingTransactionDto.getPayMode().equalsIgnoreCase("DEBIT"))) {
            // For withdrawal/debit, subtract from balance
            newBalance = savingAccount.getBalance().subtract(savingTransactionDto.getAmount());
        } else {
            // For deposit/credit, add to balance
            newBalance = savingAccount.getBalance().add(savingTransactionDto.getAmount());
        }

        // Update balance in account and set balance after transaction
        savingAccount.setBalance(newBalance);
        transaction.setBalanceAfter(newBalance);

        // Save the updated account
        savingAccountsRepository.save(savingAccount);

        // Save and return the transaction
        return savingTransactionRepository.save(transaction);
    }

    @Override
    public List<SavingTransactionEntity> findByAccountNumber(String accountNumber) {
        // Verify account exists
        savingAccountsRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Saving Account", "accountNumber", accountNumber));

        // Return transactions ordered by date (newest first)
        return savingTransactionRepository.findBySavingAccount_AccountNumberOrderByCreatedAtDesc(accountNumber);
    }
}