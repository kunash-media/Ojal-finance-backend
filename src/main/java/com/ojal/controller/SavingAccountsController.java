package com.ojal.controller;

import com.ojal.global_exception.ResourceNotFoundException;
import com.ojal.model_entity.SavingAccountsEntity;
import com.ojal.model_entity.dto.request.SavingAccountDetailsDto;
import com.ojal.model_entity.dto.request.SavingAccountUpdateDto;
import com.ojal.model_entity.dto.request.SavingAccountsDto;
import com.ojal.service.SavingAccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;

@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
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

    /**
     * Get saving account by user ID
     * GET /api/accounts/get-by-userId/{userId}
     */
    @GetMapping("/get-by-userId/{userId}")
    public ResponseEntity<SavingAccountsEntity> getAccountByUserId(@PathVariable String userId) {
        try {
            SavingAccountsEntity account = savingAccountsService.getAccountByUserId(userId);
            return ResponseEntity.ok(account);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Update saving account by user ID (PATCH operation)
     * PATCH /api/accounts/update-by-userId/{userId}
     * Note: Account number cannot be modified for security reasons
     */
    @PatchMapping("/update-by-userId/{userId}")
    public ResponseEntity<?> updateAccountByUserId(
            @PathVariable String userId,
            @RequestBody SavingAccountUpdateDto updateDto) {
        try {
            SavingAccountsEntity updatedAccount = savingAccountsService.updateAccountByUserId(userId, updateDto);
            return ResponseEntity.ok(updatedAccount);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            // Create error response map for Java 8 compatibility
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Validation Error");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            // Create error response map for Java 8 compatibility
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Internal Server Error");
            errorResponse.put("message", "An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Get all saving accounts by branch name
     * GET /api/accounts/get-all-branch?branchName={branchName}
     */
    @GetMapping("/get-all-branch")
    public ResponseEntity<?> getAllAccountsByBranch(@RequestParam String branchName) {
        try {
            List<SavingAccountsEntity> accounts = savingAccountsService.getAllAccountsByBranch(branchName);

            // Create response map for Java 8 compatibility
            Map<String, Object> response = new HashMap<>();

            if (accounts.isEmpty()) {
                response.put("message", "No saving accounts found for branch: " + branchName);
                response.put("accounts", accounts);
                response.put("count", 0);
                return ResponseEntity.ok(response);
            }

            //response.put("message", "Successfully retrieved accounts for branch: " + branchName);
            response.put("accounts", accounts);
            response.put("count", accounts.size());
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            // Create error response map for Java 8 compatibility
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid Request");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            // Create error response map for Java 8 compatibility
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Internal Server Error");
            errorResponse.put("message", "An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Delete saving account by user ID
     * DELETE /api/accounts/delete-by-userId/{userId}
     */
    @DeleteMapping("/delete-by-userId/{userId}")
    public ResponseEntity<?> deleteAccountByUserId(@PathVariable String userId) {
        try {
            String message = savingAccountsService.deleteAccountByUserId(userId);

            // Create success response map for Java 8 compatibility
            Map<String, String> response = new HashMap<>();
            response.put("message", message);
            response.put("userId", userId);
            response.put("status", "SUCCESS");
            return ResponseEntity.ok(response);

        } catch (ResourceNotFoundException e) {
            // Create error response map for Java 8 compatibility
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Account Not Found");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (IllegalStateException e) {
            // Create error response map for Java 8 compatibility
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Cannot Delete Account");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        } catch (Exception e) {
            // Create error response map for Java 8 compatibility
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Internal Server Error");
            errorResponse.put("message", "An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

}