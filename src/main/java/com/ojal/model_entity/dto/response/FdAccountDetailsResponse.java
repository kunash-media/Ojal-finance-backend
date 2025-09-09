package com.ojal.model_entity.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FdAccountDetailsResponse {

    private String createdAt;
    private String accountNumber;
    private BigDecimal principalAmount;
    private BigDecimal interestRate;
    private Integer tenureMonths;
    private BigDecimal maturityAmount;
    private LocalDate maturityDate;
    private String status; // Assuming BaseAccountEntity.AccountStatus

    // Constructor
    public FdAccountDetailsResponse(String createdAt, String accountNumber, BigDecimal principalAmount,
                                    BigDecimal interestRate, Integer tenureMonths, BigDecimal maturityAmount,
                                    LocalDate maturityDate, Object status) {
        this.createdAt = createdAt;
        this.accountNumber = accountNumber;
        this.principalAmount = principalAmount;
        this.interestRate = interestRate;
        this.tenureMonths = tenureMonths;
        this.maturityAmount = maturityAmount;
        this.maturityDate = maturityDate;
        this.status = status != null ? status.toString() : null;
    }

    // Getters and Setters
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getPrincipalAmount() {
        return principalAmount;
    }

    public void setPrincipalAmount(BigDecimal principalAmount) {
        this.principalAmount = principalAmount;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}