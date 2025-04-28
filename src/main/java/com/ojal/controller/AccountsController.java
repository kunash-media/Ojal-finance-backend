package com.ojal.controller;

import com.ojal.enum_accounts.AccountType;
import com.ojal.model_entity.FdAccountsEntity;
import com.ojal.model_entity.FdAccounts_Dto.FdAccountsDto;
import com.ojal.model_entity.LoanAccountsEntity;
import com.ojal.model_entity.LoanAccounts_Dto.LoanAccountsDto;
import com.ojal.model_entity.RdAccountsEntity;
import com.ojal.model_entity.RdAccounts_Dto.RdAccountsDto;
import com.ojal.model_entity.SavingAccountsEntity;
import com.ojal.model_entity.SavingAccounts_Dto.SavingAccountsDto;
import com.ojal.service.FdAccountsService;
import com.ojal.service.LoanAccountsService;
import com.ojal.service.RdAccountsService;
import com.ojal.service.SavingAccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<AccountResponse> createRdAccount(@PathVariable String userId, @RequestBody RdAccountsDto request) {

        RdAccountsEntity account = rdAccountService.createAccount(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AccountResponse(account.getAccountNumber(), AccountType.RD_AC));
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

    // Response class
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