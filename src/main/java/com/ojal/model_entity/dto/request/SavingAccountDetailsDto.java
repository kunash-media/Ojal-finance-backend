package com.ojal.model_entity.dto.request;

import java.math.BigDecimal;

import java.util.List;

public class SavingAccountDetailsDto {

    private Long id;
    private String name;
    private String accountNumber;
    private String createdAt;
    private String accountType;
    private BigDecimal currentBalance;
    private BigDecimal interestRate;
    private String status;
    private List<SavingTransactionDto> transactionData;

    public SavingAccountDetailsDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<SavingTransactionDto> getTransactionData() {
        return transactionData;
    }

    public void setTransactionData(List<SavingTransactionDto> transactionData) {
        this.transactionData = transactionData;
    }
}