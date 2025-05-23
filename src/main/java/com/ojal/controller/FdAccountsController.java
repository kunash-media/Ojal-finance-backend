package com.ojal.controller;

import com.ojal.model_entity.FdAccountsEntity;
import com.ojal.model_entity.dto.request.FdAccountsDto;
import com.ojal.service.FdAccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/fds")
public class FdAccountsController {

    private final FdAccountsService fdAccountsService;

    @Autowired
    public FdAccountsController(FdAccountsService fdAccountsService) {
        this.fdAccountsService = fdAccountsService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<FdAccountsEntity> createFdAccount(
            @PathVariable String userId,
            @RequestBody FdAccountsDto request) {
        FdAccountsEntity createdAccount = fdAccountsService.createAccount(userId, request);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<FdAccountsEntity> getFdAccount(@PathVariable String accountNumber) {
        FdAccountsEntity account = fdAccountsService.findByAccountNumber(accountNumber);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FdAccountsEntity>> getFdAccountsByUser(@PathVariable String userId) {
        List<FdAccountsEntity> accounts = fdAccountsService.findAllByUserId(userId);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping
    public ResponseEntity<List<FdAccountsEntity>> getAllFdAccounts() {
        List<FdAccountsEntity> accounts = fdAccountsService.findAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    @PutMapping("/{accountNumber}")
    public ResponseEntity<FdAccountsEntity> updateFdAccount(
            @PathVariable String accountNumber,
            @RequestBody FdAccountsDto request) {
        FdAccountsEntity updatedAccount = fdAccountsService.updateAccount(accountNumber, request);
        return ResponseEntity.ok(updatedAccount);
    }

    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<Void> deleteFdAccount(@PathVariable String accountNumber) {
        fdAccountsService.deleteAccount(accountNumber);
        return ResponseEntity.noContent().build();
    }


}
