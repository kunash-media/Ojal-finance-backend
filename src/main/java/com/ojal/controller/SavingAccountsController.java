package com.ojal.controller;

import com.ojal.model_entity.SavingAccountsEntity;
import com.ojal.model_entity.dto.request.SavingAccountDetailsDto;
import com.ojal.model_entity.dto.request.SavingAccountsDto;
import com.ojal.service.SavingAccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/saving")
public class SavingAccountsController {

    private final SavingAccountsService savingAccountsService;

    @Autowired
    public SavingAccountsController(SavingAccountsService savingAccountsService) {
        this.savingAccountsService = savingAccountsService;
    }

    @PostMapping("/users/{userId}")
    public ResponseEntity<SavingAccountsEntity> createAccount(
            @PathVariable String userId,
            @RequestBody SavingAccountsDto request) {
        return new ResponseEntity<>(
                savingAccountsService.createAccount(userId, request),
                HttpStatus.CREATED);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<SavingAccountsEntity> getAccount(@PathVariable String accountNumber) {
        return ResponseEntity.ok(savingAccountsService.findByAccountNumber(accountNumber));
    }

    @GetMapping("/{accountNumber}/details")
    public ResponseEntity<SavingAccountDetailsDto> getAccountWithTransactions(@PathVariable String accountNumber) {
        return ResponseEntity.ok(savingAccountsService.getAccountWithTransactions(accountNumber));
    }
}