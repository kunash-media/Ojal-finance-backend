package com.ojal.controller;

import com.ojal.enum_accounts.AccountType;
import com.ojal.model_entity.RdAccountsEntity;
import com.ojal.model_entity.UsersEntity;
import com.ojal.model_entity.dto.request.RdAccountUpdateDto;
import com.ojal.model_entity.dto.request.RdAccountsDto;
import com.ojal.model_entity.dto.response.RdAccountDetailsResponse;
import com.ojal.model_entity.dto.response.RdUserResponse;
import com.ojal.service.RdAccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rds")
public class RdAccountsController {

    private final RdAccountsService rdAccountService;

    @Autowired
    public RdAccountsController(RdAccountsService rdAccountService) {
        this.rdAccountService = rdAccountService;
    }

    @PostMapping("/create-rd/{userId}")
    public ResponseEntity<RdUserResponse> createRdAccount(
            @PathVariable String userId,
            @RequestBody RdAccountsDto request) {

        UsersEntity user = rdAccountService.createAccount(userId, request);

        RdUserResponse response = new RdUserResponse();
        response.setUserId(user.getUserId());
        response.setName(user.getFirstName());

        // Convert RD accounts to AccountResponse list
        List<AccountResponse> accountResponses = user.getRdAccounts().stream()
                .map(account -> new AccountResponse(
                        account.getAccountNumber(),
                        AccountType.RD_AC))
                .collect(Collectors.toList());
        response.setRdAccounts(accountResponses);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/get-rd-by-accNum/{accountNumber}")
    public ResponseEntity<RdAccountDetailsResponse> getRdAccountByNumber(
            @PathVariable String accountNumber) {

        RdAccountsEntity account = rdAccountService.findByAccountNumber(accountNumber);

        RdAccountDetailsResponse response = new RdAccountDetailsResponse(
                account.getCreatedAt(),
                account.getAccountNumber(),
                account.getDepositAmount(),
                account.getInterestRate(),
                account.getTenureMonths(),
                account.getMaturityAmount(),
                account.getMaturityDate(),
                account.getStatus(),
                account.getPayoutAmount(),
                account.getInterestEarned(),
                account.getPenaltyApplied(),
                account.getWithdrawnDate(),
                "RD account fetched successfully"
        );

        return ResponseEntity.ok(response);
    }


    @GetMapping("/get-all-rds-by-userId/{userId}")
    public ResponseEntity<List<RdAccountDetailsResponse>> getAllRdAccountsByUserId(
            @PathVariable String userId) {

        List<RdAccountsEntity> accounts = rdAccountService.findAllByUserId(userId);

        List<RdAccountDetailsResponse> response = accounts.stream()
                .map(account -> new RdAccountDetailsResponse(
                        account.getCreatedAt(),
                        account.getAccountNumber(),
                        account.getDepositAmount(),
                        account.getInterestRate(),
                        account.getTenureMonths(),
                        account.getMaturityAmount(),
                        account.getMaturityDate(),
                        account.getStatus(),
                        account.getPayoutAmount(),
                        account.getInterestEarned(),
                        account.getPenaltyApplied(),
                        account.getWithdrawnDate(),
                        "RD account fetched successfully"
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // ------------ process monthly deposit with penalties -----------//
    @PutMapping("/{accountNumber}/deposit")
    public ResponseEntity<RdAccountDetailsResponse> processMonthlyDeposit(
            @PathVariable String accountNumber) {

        RdAccountsEntity account = rdAccountService.processMonthlyDeposit(accountNumber);

        RdAccountDetailsResponse response = new RdAccountDetailsResponse(
                account.getCreatedAt(),
                account.getAccountNumber(),
                account.getDepositAmount(),
                account.getInterestRate(),
                account.getTenureMonths(),
                account.getMaturityAmount(),
                account.getMaturityDate(),
                account.getStatus(),
                account.getPayoutAmount(),
                account.getInterestEarned(),
                account.getPenaltyApplied(),
                account.getWithdrawnDate(),
                "RD account fetched successfully"
        );

        return ResponseEntity.ok(response);
    }

    // ------------ NEW PATCH API -----------//
    @PatchMapping("/patch-rd-by-accNum/{accountNumber}")
    public ResponseEntity<RdAccountDetailsResponse> updateRdAccount(
            @PathVariable String accountNumber,
            @RequestBody RdAccountUpdateDto updateRequest) {

        RdAccountsEntity account = rdAccountService.updateRdAccount(accountNumber, updateRequest);

        RdAccountDetailsResponse response = new RdAccountDetailsResponse(
                account.getCreatedAt(),
                account.getAccountNumber(),
                account.getDepositAmount(),
                account.getInterestRate(),
                account.getTenureMonths(),
                account.getMaturityAmount(),
                account.getMaturityDate(),
                account.getStatus(),
                account.getPayoutAmount(),
                account.getInterestEarned(),
                account.getPenaltyApplied(),
                account.getWithdrawnDate(),
                "RD account fetched successfully"
        );

        return ResponseEntity.ok(response);
    }

    // ------------ DELETE APIS -----------//
    @DeleteMapping("/delete-rd-by-accNum/{accountNumber}")
    public ResponseEntity<String> deleteRdAccountByNumber(
            @PathVariable String accountNumber) {

        rdAccountService.deleteByAccountNumber(accountNumber);
        return ResponseEntity.ok("RD account deleted successfully with account number: " + accountNumber);
    }

    @DeleteMapping("/delete-all-rds-by-userId/{userId}")
    public ResponseEntity<String> deleteAllRdAccountsByUserId(
            @PathVariable String userId) {

        int deletedCount = rdAccountService.deleteAllByUserId(userId);
        return ResponseEntity.ok("Successfully deleted " + deletedCount + " RD accounts for user: " + userId);
    }

    @PostMapping("/withdraw/{accountNumber}")
    public ResponseEntity<Map<String, Object>> withdrawRd(@PathVariable String accountNumber) {
        RdAccountsEntity account = rdAccountService.withdrawRdAccount(accountNumber);

        Map<String, Object> response = new HashMap<>();
        response.put("accountNumber", account.getAccountNumber());
        response.put("depositAmount", account.getDepositAmount());
        response.put("tenureMonths", account.getTenureMonths());
        response.put("interestEarned", account.getInterestEarned());
        response.put("penaltyApplied", account.getPenaltyApplied());
        response.put("payoutAmount", account.getMaturityAmount());
        response.put("withdrawnDate", account.getWithdrawnDate());
        response.put("status", account.getStatus());
        response.put("message", "RD withdrawn successfully");

        return ResponseEntity.ok(response);
    }




    // This inner class should replace usage of AccountsController.AccountResponse
    public static class AccountResponse {
        private String accountNumber;
        private AccountType accountType;

        public AccountResponse(String accountNumber, AccountType accountType) {
            this.accountNumber = accountNumber;
            this.accountType = accountType;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public AccountType getAccountType() {
            return accountType;
        }

        public void setAccountType(AccountType accountType) {
            this.accountType = accountType;
        }
    }
}