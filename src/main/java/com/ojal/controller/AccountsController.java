package com.ojal.controller;

import com.ojal.enum_accounts.AccountType;
import com.ojal.model_entity.*;
import com.ojal.model_entity.dto.request.FdAccountsDto;
import com.ojal.model_entity.dto.request.LoanAccountsDto;
import com.ojal.model_entity.dto.request.RdAccountsDto;
import com.ojal.model_entity.dto.request.SavingAccountsDto;
import com.ojal.model_entity.dto.response.RdUserResponse;
import com.ojal.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accounts")
public class AccountsController {

    private final SavingAccountsService savingAccountService;
    private final RdAccountsService rdAccountService;
    private final FdAccountsService fdAccountService;
    private final LoanAccountsService loanAccountService;

    @Autowired
    public AccountsController(
            SavingAccountsService savingAccountService,
            RdAccountsService rdAccountService,
            FdAccountsService fdAccountService,
            LoanAccountsService loanAccountService
    ) {
        this.savingAccountService = savingAccountService;
        this.rdAccountService = rdAccountService;
        this.fdAccountService = fdAccountService;
        this.loanAccountService = loanAccountService;
    }

    @PostMapping("/{userId}/saving")
    public ResponseEntity<AccountResponse> createSavingAccount(@PathVariable String userId, @RequestBody SavingAccountsDto request) {

        SavingAccountsEntity account = savingAccountService.createAccount(userId, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AccountResponse(account.getAccountNumber(), AccountType.SAVING_AC));
    }

    @PostMapping("/{userId}/rd")
    public ResponseEntity<RdUserResponse> createRdAccount(
            @PathVariable String userId,
            @RequestBody RdAccountsDto request) {

        // 1. Create RD account and get updated user
        UsersEntity user = rdAccountService.createAccount(userId, request);

        // 2. Convert to RdUserResponse
        RdUserResponse response = new RdUserResponse();
        response.setUserId(user.getUserId());
        response.setName(user.getName()); // Or other user fields you want to include

        // 3. Convert RD accounts to AccountResponse list
        List<AccountResponse> accountResponses = user.getRdAccounts().stream()
                .map(account -> new AccountsController.AccountResponse(
                        account.getAccountNumber(),
                        AccountType.RD_AC))
                .collect(Collectors.toList());
        response.setRdAccounts(accountResponses);

        // 4. Return response
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PostMapping("/{userId}/fd")
    public ResponseEntity<AccountResponse> createFdAccount(@PathVariable String userId, @RequestBody FdAccountsDto request) {

        FdAccountsEntity account = fdAccountService.createAccount(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AccountResponse(account.getAccountNumber(), AccountType.FD_AC));
    }

    @PostMapping("/{userId}/loan")
    public ResponseEntity<AccountResponse> createLoanAccount(@PathVariable String userId, @RequestBody LoanAccountsDto request) {

        LoanAccountsEntity account = loanAccountService.createAccount(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AccountResponse(account.getAccountNumber(), AccountType.LOAN_AC));
    }



    // --------------  Response class  ---------------------//
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