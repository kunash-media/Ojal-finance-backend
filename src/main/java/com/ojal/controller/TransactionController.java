package com.ojal.controller;

import com.ojal.model_entity.TransactionEntity;
import com.ojal.model_entity.dto.request.TransactionDto;
import com.ojal.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/saving/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/{accountNumber}")
    public ResponseEntity<TransactionEntity> createTransaction(
            @PathVariable String accountNumber,
            @RequestBody TransactionDto request) {
        return new ResponseEntity<>(
                transactionService.createTransaction(accountNumber, request),
                HttpStatus.CREATED);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<List<TransactionEntity>> getTransactions(@PathVariable String accountNumber) {
        return ResponseEntity.ok(transactionService.findByAccountNumber(accountNumber));
    }
}