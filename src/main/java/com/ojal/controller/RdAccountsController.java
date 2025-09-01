package com.ojal.controller;

import com.ojal.enum_accounts.AccountType;
import com.ojal.model_entity.BaseAccountEntity;
import com.ojal.model_entity.RdAccountsEntity;
import com.ojal.model_entity.UsersEntity;
import com.ojal.model_entity.dto.request.RdAccountsDto;
import com.ojal.model_entity.dto.response.RdUserResponse;
import com.ojal.service.RdAccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rds")
public class RdAccountsController {

    private final RdAccountsService rdAccountService;

    @Autowired
    public RdAccountsController(RdAccountsService rdAccountService) {
        this.rdAccountService = rdAccountService;
    }

    @PostMapping("/{userId}")
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

    @GetMapping("/{accountNumber}")
    public ResponseEntity<RdAccountDetailsResponse> getRdAccountByNumber(
            @PathVariable String accountNumber) {

        RdAccountsEntity account = rdAccountService.findByAccountNumber(accountNumber);

        RdAccountDetailsResponse response = new RdAccountDetailsResponse(
                account.getAccountNumber(),
                account.getDepositAmount(),
                account.getInterestRate(),
                account.getTenureMonths(),
                account.getMaturityAmount(),
                account.getMaturityDate(),
                account.getStatus()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RdAccountDetailsResponse>> getAllRdAccountsByUserId(
            @PathVariable String userId) {

        List<RdAccountsEntity> accounts = rdAccountService.findAllByUserId(userId);

        List<RdAccountDetailsResponse> response = accounts.stream()
                .map(account -> new RdAccountDetailsResponse(
                        account.getAccountNumber(),
                        account.getDepositAmount(),
                        account.getInterestRate(),
                        account.getTenureMonths(),
                        account.getMaturityAmount(),
                        account.getMaturityDate(),
                        account.getStatus()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{accountNumber}/deposit")
    public ResponseEntity<RdAccountDetailsResponse> processMonthlyDeposit(
            @PathVariable String accountNumber) {

        RdAccountsEntity account = rdAccountService.processMonthlyDeposit(accountNumber);

        RdAccountDetailsResponse response = new RdAccountDetailsResponse(
                account.getAccountNumber(),
                account.getDepositAmount(),
                account.getInterestRate(),
                account.getTenureMonths(),
                account.getMaturityAmount(),
                account.getMaturityDate(),
                account.getStatus()
        );

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

    // New response class for RD account details
    public static class RdAccountDetailsResponse {
        private String accountNumber;
        private BigDecimal depositAmount;
        private BigDecimal interestRate;
        private Integer tenureMonths;
        private BigDecimal maturityAmount;
        private LocalDate maturityDate;
        private BaseAccountEntity.AccountStatus status;

        public RdAccountDetailsResponse(
                String accountNumber,
                BigDecimal depositAmount,
                BigDecimal interestRate,
                Integer tenureMonths,
                BigDecimal maturityAmount,
                LocalDate maturityDate,
                BaseAccountEntity.AccountStatus status) {
            this.accountNumber = accountNumber;
            this.depositAmount = depositAmount;
            this.interestRate = interestRate;
            this.tenureMonths = tenureMonths;
            this.maturityAmount = maturityAmount;
            this.maturityDate = maturityDate;
            this.status = status;
        }

        // Getters and setters
        public String getAccountNumber() {
            return accountNumber;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public BigDecimal getDepositAmount() {
            return depositAmount;
        }

        public void setDepositAmount(BigDecimal depositAmount) {
            this.depositAmount = depositAmount;
        }

        public BigDecimal getInterestRate() {
            return interestRate;
        }

        public void setInterestRate(BigDecimal interestRate) {
            this.interestRate = interestRate;
        }

        public Integer getTenureMonths() {
            return tenureMonths;
        }

        public void setTenureMonths(Integer tenureMonths) {
            this.tenureMonths = tenureMonths;
        }

        public BigDecimal getMaturityAmount() {
            return maturityAmount;
        }

        public void setMaturityAmount(BigDecimal maturityAmount) {
            this.maturityAmount = maturityAmount;
        }

        public LocalDate getMaturityDate() {
            return maturityDate;
        }

        public void setMaturityDate(LocalDate maturityDate) {
            this.maturityDate = maturityDate;
        }

        public BaseAccountEntity.AccountStatus getStatus() {
            return status;
        }

        public void setStatus(BaseAccountEntity.AccountStatus status) {
            this.status = status;
        }
    }
}