package com.ojal.controller;

import com.ojal.model_entity.FdAccountsEntity;
import com.ojal.model_entity.FdTransactionEntity;
import com.ojal.model_entity.dto.request.FdTransactionDTO;
import com.ojal.service.FdAccountsService;
import com.ojal.service.FdTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/fd-transactions")
public class FdTransactionController {

    private final FdTransactionService fdTransactionService;
    private final FdAccountsService fdAccountsService;

    @Autowired
    public FdTransactionController(FdTransactionService fdTransactionService,
                                   FdAccountsService fdAccountsService) {
        this.fdTransactionService = fdTransactionService;
        this.fdAccountsService = fdAccountsService;
    }

    // Controller Method
    @PostMapping("create/{accountNumber}")
    public ResponseEntity<?> createTransaction(@PathVariable String accountNumber,
                                               @RequestBody FdTransactionDTO transactionDTO) {
        try {
            // Set the account number from path variable
            transactionDTO.setAccountNumber(accountNumber);
            FdTransactionEntity transaction = fdTransactionService.createTransaction(transactionDTO);
            return new ResponseEntity<>(transaction, HttpStatus.CREATED);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Get transaction by ID
     * @param id Transaction ID
     * @return ResponseEntity with transaction data
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getTransactionById(@PathVariable Long id) {
        try {
            Optional<FdTransactionEntity> transaction = fdTransactionService.getTransactionById(id);

            if (transaction.isPresent()) {
                return ResponseEntity.ok(transaction.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Internal Error", "Failed to retrieve transaction"));
        }
    }

    /**
     * Get transaction by transaction ID
     * @param transactionId Unique transaction identifier
     * @return ResponseEntity with transaction data
     */
    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<?> getTransactionByTransactionId(@PathVariable String transactionId) {
        try {
            Optional<FdTransactionEntity> transaction = fdTransactionService.getTransactionByTransactionId(transactionId);

            if (transaction.isPresent()) {
                return ResponseEntity.ok(transaction.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Internal Error", "Failed to retrieve transaction"));
        }
    }

    /**
     * Get FD account details with all transactions by account number
     * @param accountNumber FD account number
     * @return ResponseEntity with account and transaction details
     */
    @GetMapping("/account/{accountNumber}/details")
    public ResponseEntity<?> getFdAccountWithTransactions(@PathVariable String accountNumber) {
        try {
            // Get FD account by account number (adjust method name based on your service)
            FdAccountsEntity fdAccountOpt = fdAccountsService.findByAccountNumber(accountNumber);

            if (fdAccountOpt != null) {
//                FdAccountsEntity fdAccount = fdAccountOpt;

                // Get all transactions for this account
                List<FdTransactionEntity> transactions = fdTransactionService.getTransactionsByAccountNumber(accountNumber);

                // Create response payload similar to the saving account structure
                Map<String, Object> response = new HashMap<>();
                response.put("name", fdAccountOpt.getUser().getFirstName() + " " + fdAccountOpt.getUser().getLastName());
                response.put("accountNumber", fdAccountOpt.getAccountNumber());
                response.put("createdAt", fdAccountOpt.getCreatedAt());
                response.put("accountType", "FD_AC");
                response.put("principalAmount", fdAccountOpt.getPrincipalAmount());
                response.put("interestRate", fdAccountOpt.getInterestRate());
                response.put("tenureMonths", fdAccountOpt.getTenureMonths());
                response.put("maturityDate", fdAccountOpt.getMaturityDate());
                response.put("maturityAmount", fdAccountOpt.getMaturityAmount());
                response.put("status", fdAccountOpt.getStatus());

                // Format transaction data
                List<Map<String, Object>> transactionData = transactions.stream()
                        .map(this::formatTransactionForResponse)
                        .toList();

                response.put("transactionData", transactionData);

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Internal Error", "Failed to retrieve account details"));
        }
    }

    /**
     * Get all transactions for a specific FD account number
     * @param accountNumber FD account number
     * @return ResponseEntity with list of transactions
     */
    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<?> getTransactionsByAccountNumber(@PathVariable String accountNumber) {
        try {
            List<FdTransactionEntity> transactions = fdTransactionService.getTransactionsByAccountNumber(accountNumber);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Internal Error", "Failed to retrieve transactions"));
        }
    }

    /**
     * Get all transactions
     * @return ResponseEntity with list of all transactions
     */
    @GetMapping
    public ResponseEntity<?> getAllTransactions() {
        try {
            List<FdTransactionEntity> transactions = fdTransactionService.getAllTransactions();
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Internal Error", "Failed to retrieve transactions"));
        }
    }

    /**
     * Get transactions by status
     * @param status Transaction status
     * @return ResponseEntity with filtered transactions
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getTransactionsByStatus(@PathVariable String status) {
        try {
            FdTransactionEntity.TransactionStatus transactionStatus = FdTransactionEntity.TransactionStatus.valueOf(status.toUpperCase());
            List<FdTransactionEntity> transactions = fdTransactionService.getTransactionsByStatus(transactionStatus);
            return ResponseEntity.ok(transactions);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Invalid Status", "Invalid transaction status: " + status));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Internal Error", "Failed to retrieve transactions"));
        }
    }

    /**
     * Get transactions by pay mode
     * @param payMode Payment mode
     * @return ResponseEntity with filtered transactions
     */
    @GetMapping("/paymode/{payMode}")
    public ResponseEntity<?> getTransactionsByPayMode(@PathVariable String payMode) {
        try {
            FdTransactionEntity.PayMode payModeEnum = FdTransactionEntity.PayMode.valueOf(payMode.toUpperCase());
            List<FdTransactionEntity> transactions = fdTransactionService.getTransactionsByPayMode(payModeEnum);
            return ResponseEntity.ok(transactions);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Invalid Pay Mode", "Invalid pay mode: " + payMode));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Internal Error", "Failed to retrieve transactions"));
        }
    }

    /**
     * Update a transaction
     * @param id Transaction ID
     * @param updatedTransaction Updated transaction data
     * @return ResponseEntity with updated transaction
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTransaction(@PathVariable Long id, @RequestBody FdTransactionEntity updatedTransaction) {
        try {
            FdTransactionEntity transaction = fdTransactionService.updateTransaction(id, updatedTransaction);
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Internal Error", "Failed to update transaction"));
        }
    }

    /**
     * Update transaction status
     * @param transactionId Unique transaction identifier
     * @param status New transaction status
     * @return ResponseEntity with updated transaction
     */
    @PatchMapping("/transaction/{transactionId}/status")
    public ResponseEntity<?> updateTransactionStatus(@PathVariable String transactionId, @RequestParam String status) {
        try {
            FdTransactionEntity.TransactionStatus transactionStatus = FdTransactionEntity.TransactionStatus.valueOf(status.toUpperCase());
            FdTransactionEntity transaction = fdTransactionService.updateTransactionStatus(transactionId, transactionStatus);
            return ResponseEntity.ok(transaction);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Invalid Status", "Invalid transaction status: " + status));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Internal Error", "Failed to update transaction status"));
        }
    }

    /**
     * Delete transaction by ID
     * @param id Transaction ID
     * @return ResponseEntity with success message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable Long id) {
        try {
            fdTransactionService.deleteTransaction(id);
            return ResponseEntity.ok(createSuccessResponse("Transaction deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Internal Error", "Failed to delete transaction"));
        }
    }

    /**
     * Delete transaction by transaction ID
     * @param transactionId Unique transaction identifier
     * @return ResponseEntity with success message
     */
    @DeleteMapping("/transaction/{transactionId}")
    public ResponseEntity<?> deleteTransactionByTransactionId(@PathVariable String transactionId) {
        try {
            fdTransactionService.deleteTransactionByTransactionId(transactionId);
            return ResponseEntity.ok(createSuccessResponse("Transaction deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Internal Error", "Failed to delete transaction"));
        }
    }


    /**
     * Check if transaction exists by transaction ID
     * @param transactionId Unique transaction identifier
     * @return ResponseEntity with existence status
     */
    @GetMapping("/exists/{transactionId}")
    public ResponseEntity<?> checkTransactionExists(@PathVariable String transactionId) {
        try {
            boolean exists = fdTransactionService.existsByTransactionId(transactionId);
            Map<String, Object> response = new HashMap<>();
            response.put("transactionId", transactionId);
            response.put("exists", exists);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Internal Error", "Failed to check transaction existence"));
        }
    }

    /**
     * Format transaction data for API response
     * @param transaction Transaction entity
     * @return Formatted transaction map
     */
    private Map<String, Object> formatTransactionForResponse(FdTransactionEntity transaction) {
        Map<String, Object> transactionMap = new HashMap<>();
        transactionMap.put("date", transaction.getCreatedAt());
        transactionMap.put("amount", transaction.getAmount());
        transactionMap.put("payMode", transaction.getPayMode().toString());
        transactionMap.put("transactionId", transaction.getTransactionId());
        transactionMap.put("note", transaction.getNote());
        transactionMap.put("balanceAfter", transaction.getBalanceAfter());
        transactionMap.put("status", transaction.getStatus().toString());
        return transactionMap;
    }

    /**
     * Create error response map
     * @param error Error type
     * @param message Error message
     * @return Error response map
     */
    private Map<String, String> createErrorResponse(String error, String message) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", error);
        errorResponse.put("message", message);
        return errorResponse;
    }

    /**
     * Create success response map
     * @param message Success message
     * @return Success response map
     */
    private Map<String, String> createSuccessResponse(String message) {
        Map<String, String> successResponse = new HashMap<>();
        successResponse.put("status", "success");
        successResponse.put("message", message);
        return successResponse;
    }
}