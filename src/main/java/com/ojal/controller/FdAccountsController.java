package com.ojal.controller;

import com.ojal.model_entity.FdAccountsEntity;
import com.ojal.model_entity.dto.request.FdAccountUpdateDto;
import com.ojal.model_entity.dto.request.FdAccountsDto;
import com.ojal.model_entity.dto.request.WithdrawRequest;
import com.ojal.model_entity.dto.response.FdAccountDetailsResponse;
import com.ojal.model_entity.dto.response.WithdrawResponse;
import com.ojal.service.FdAccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/fds")
public class FdAccountsController {

    private final FdAccountsService fdAccountsService;

    @Autowired
    public FdAccountsController(FdAccountsService fdAccountsService) {
        this.fdAccountsService = fdAccountsService;
    }

    @PostMapping("/create-fd/{userId}")
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

    @GetMapping("/get-all-fds")
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

    // ------------ NEW APIS MATCHING RD PATTERN -----------//
    @GetMapping("/get-fd-by-accNum/{accountNumber}")
    public ResponseEntity<FdAccountDetailsResponse> getFdAccountByNumber(
            @PathVariable String accountNumber) {

        FdAccountsEntity account = fdAccountsService.findByAccountNumber(accountNumber);

        FdAccountDetailsResponse response = new FdAccountDetailsResponse(
                account.getCreatedAt(),
                account.getAccountNumber(),
                account.getPrincipalAmount(),
                account.getInterestRate(),
                account.getTenureMonths(),
                account.getMaturityAmount(),
                account.getMaturityDate(),
                account.getStatus()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all-fds-by-userId/{userId}")
    public ResponseEntity<List<FdAccountDetailsResponse>> getAllFdAccountsByUserId(
            @PathVariable String userId) {

        List<FdAccountsEntity> accounts = fdAccountsService.findAllByUserId(userId);

        List<FdAccountDetailsResponse> response = accounts.stream()
                .map(account -> new FdAccountDetailsResponse(
                        account.getCreatedAt(),
                        account.getAccountNumber(),
                        account.getPrincipalAmount(),
                        account.getInterestRate(),
                        account.getTenureMonths(),
                        account.getMaturityAmount(),
                        account.getMaturityDate(),
                        account.getStatus()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/patch-fd-by-accNum/{accountNumber}")
    public ResponseEntity<FdAccountDetailsResponse> updateFdAccountPartial(
            @PathVariable String accountNumber,
            @RequestBody FdAccountUpdateDto updateRequest) {

        FdAccountsEntity account = fdAccountsService.updateFdAccountPartial(accountNumber, updateRequest);

        FdAccountDetailsResponse response = new FdAccountDetailsResponse(
                account.getCreatedAt(),
                account.getAccountNumber(),
                account.getPrincipalAmount(),
                account.getInterestRate(),
                account.getTenureMonths(),
                account.getMaturityAmount(),
                account.getMaturityDate(),
                account.getStatus()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete-fd-by-accNum/{accountNumber}")
    public ResponseEntity<String> deleteFdAccountByNumber(
            @PathVariable String accountNumber) {

        fdAccountsService.deleteByAccountNumber(accountNumber);
        return ResponseEntity.ok("FD account deleted successfully with account number: " + accountNumber);
    }

    @DeleteMapping("/delete-all-fds-by-userId/{userId}")
    public ResponseEntity<String> deleteAllFdAccountsByUserId(
            @PathVariable String userId) {

        int deletedCount = fdAccountsService.deleteAllByUserId(userId);
        return ResponseEntity.ok("Successfully deleted " + deletedCount + " FD accounts for user: " + userId);
    }


    @PostMapping("/withdraw/{accountNumber}")
    public ResponseEntity<WithdrawResponse> withdrawFd(
            @PathVariable String accountNumber,
            @RequestBody(required = false) WithdrawRequest request) {

        FdAccountsEntity updated = fdAccountsService.withdrawFd(accountNumber, request);

        // compute values for response from updated entity
        WithdrawResponse resp = new WithdrawResponse();
        resp.setAccountNumber(updated.getAccountNumber());
        resp.setPrincipalAmount(updated.getPrincipalAmount());
        resp.setPenaltyApplied(updated.getPenaltyApplied() == null ? BigDecimal.ZERO : updated.getPenaltyApplied());
        resp.setPayoutAmount(updated.getPayoutAmount() == null ? BigDecimal.ZERO : updated.getPayoutAmount());
        resp.setWithdrawnDate(updated.getWithdrawnDate());
        resp.setStatus(updated.getStatus().name());
        resp.setMessage(updated.getIsWithdrawn() ? "Withdrawn successfully" : "Withdraw attempted");

        // calculate interest earned if you want also included separately
        BigDecimal interestEarned = resp.getPayoutAmount().subtract(resp.getPrincipalAmount()).add(resp.getPenaltyApplied().negate());
        // but interestEarned may be null if payout is zero; safe-guard:
        resp.setInterestEarned(
                resp.getPayoutAmount() != null && resp.getPrincipalAmount() != null
                        ? resp.getPayoutAmount().subtract(resp.getPrincipalAmount()).add(resp.getPenaltyApplied().negate())
                        : BigDecimal.ZERO
        );

        return ResponseEntity.ok(resp);
    }

}
