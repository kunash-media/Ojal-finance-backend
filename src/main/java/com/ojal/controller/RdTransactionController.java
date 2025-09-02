package com.ojal.controller;

import com.ojal.model_entity.RdAccountsEntity;
import com.ojal.model_entity.RdTransactionEntity;
import com.ojal.model_entity.dto.request.RdTransactionDTO;
import com.ojal.model_entity.dto.response.RdAccountResponseDTO;
import com.ojal.repository.RdAccountsRepository;
import com.ojal.service.RdTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rd/transactions")
public class RdTransactionController {

    @Autowired
    private RdTransactionService transactionService;

    @Autowired
    private RdAccountsRepository rdAccountsRepository;

    // Create a new transaction for an RD account
    @PostMapping("/rd-deposit/{accountNumber}")
    public ResponseEntity<?> createTransaction(@PathVariable String accountNumber, @RequestBody RdTransactionDTO transactionDTO) {
        try {
            // Set the account number from path variable
            // transactionDTO.setAccountNumber(accountNumber);
            RdTransactionEntity transaction = transactionService.createTransaction(accountNumber,transactionDTO);
            return new ResponseEntity<>(transaction, HttpStatus.CREATED);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all transactions for an RD account
    @GetMapping("/get-rd-transactions/{accountNumber}")
    public ResponseEntity<?> getTransactionsByAccountNumber(@PathVariable String accountNumber) {
        try {
            // Get the RD account
            RdAccountsEntity rdAccount = rdAccountsRepository.findByAccountNumber(accountNumber)
                    .orElseThrow(() -> new NoSuchElementException("RD Account not found with account number: " + accountNumber));

            // Get transactions
            List<RdTransactionEntity> transactions = transactionService.getTransactionsByAccountNumber(accountNumber);

            // Map to response DTO
            RdAccountResponseDTO responseDTO = new RdAccountResponseDTO();
            responseDTO.setId(rdAccount.getId());
            responseDTO.setName(rdAccount.getUser().getFirstName());
            responseDTO.setAccountNumber(rdAccount.getAccountNumber());
            responseDTO.setCreatedAt(rdAccount.getCreatedAt());
            responseDTO.setDepositAmount(rdAccount.getDepositAmount());
            responseDTO.setInterestRate(rdAccount.getInterestRate());
            responseDTO.setTenureMonths(rdAccount.getTenureMonths());
            responseDTO.setMaturityDate(rdAccount.getMaturityDate());
            responseDTO.setMaturityAmount(rdAccount.getMaturityAmount());
            responseDTO.setStatus(rdAccount.getStatus().toString());

            // Map transactions
            List<RdAccountResponseDTO.RdTransactionResponseDTO> transactionDataList = transactions.stream()
                    .map(transaction -> {
                        RdAccountResponseDTO.RdTransactionResponseDTO transactionDTO = new RdAccountResponseDTO.RdTransactionResponseDTO();
                        transactionDTO.setDate(transaction.getCreatedAt());
                        transactionDTO.setAmount(transaction.getAmount());
                        transactionDTO.setPayMode(transaction.getPayMode());
                        transactionDTO.setUtrNo(transaction.getUtrNo());
                        transactionDTO.setCash(transaction.getCash());
                        transactionDTO.setChequeNumber(transaction.getChequeNumber());
                        transactionDTO.setNote(transaction.getNote());
                        transactionDTO.setBalanceAfter(transaction.getBalanceAfter());
                        transactionDTO.setStatus(transaction.getStatus().toString());
                        return transactionDTO;
                    })
                    .collect(Collectors.toList());

            responseDTO.setTransactionData(transactionDataList);

            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get transaction by ID
    @GetMapping("/get-transaction-by-id/{id}")
    public ResponseEntity<?> getTransactionById(@PathVariable Long id) {
        try {
            RdTransactionEntity transaction = transactionService.getTransactionById(id);
            return new ResponseEntity<>(transaction, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update transaction status
    @PutMapping("/update-transaction-status/{id}/status")
    public ResponseEntity<?> updateTransactionStatus(
            @PathVariable Long id,
            @RequestParam RdTransactionEntity.TransactionStatus status) {
        try {
            RdTransactionEntity updatedTransaction = transactionService.updateTransactionStatus(id, status);
            return new ResponseEntity<>(updatedTransaction, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete transaction
    @DeleteMapping("/delete-transaction/{id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable Long id) {
        try {
            transactionService.deleteTransaction(id);
            return new ResponseEntity<>("Transaction deleted successfully", HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}