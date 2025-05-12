package com.ojal.controller;

import com.ojal.model_entity.SavingTransactionEntity;
import com.ojal.model_entity.dto.request.SavingTransactionDto;
import com.ojal.service.SavingTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/saving/transactions")
public class SavingTransactionController {

    private final SavingTransactionService savingTransactionService;

    @Autowired
    public SavingTransactionController(SavingTransactionService transactionService) {
        this.savingTransactionService = transactionService;
    }

    @PostMapping("/{accountNumber}")
    public ResponseEntity<SavingTransactionEntity> createTransaction(
            @PathVariable String accountNumber,
            @RequestBody SavingTransactionDto request) {
        return new ResponseEntity<>(
                savingTransactionService.createTransaction(accountNumber, request),
                HttpStatus.CREATED);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<List<SavingTransactionEntity>> getTransactions(@PathVariable String accountNumber) {
        return ResponseEntity.ok(savingTransactionService.findByAccountNumber(accountNumber));
    }
}