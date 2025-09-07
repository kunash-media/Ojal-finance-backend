package com.ojal.model_entity.dto.response;

import com.ojal.model_entity.BaseAccountEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RdAccountDetailsResponse{
    private String createdAt;
    private String accountNumber;
    private BigDecimal depositAmount;
    private BigDecimal interestRate;
    private Integer tenureMonths;
    private BigDecimal maturityAmount;
    private LocalDate maturityDate;
    private BaseAccountEntity.AccountStatus status;

    public RdAccountDetailsResponse(
            String createdAt,
            String accountNumber,
            BigDecimal depositAmount,
            BigDecimal interestRate,
            Integer tenureMonths,
            BigDecimal maturityAmount,
            LocalDate maturityDate,
            BaseAccountEntity.AccountStatus status) {
        this.createdAt = createdAt;
        this.accountNumber = accountNumber;
        this.depositAmount = depositAmount;
        this.interestRate = interestRate;
        this.tenureMonths = tenureMonths;
        this.maturityAmount = maturityAmount;
        this.maturityDate = maturityDate;
        this.status = status;
    }

    // Getters and setters


    public String getCreatedAt() {return createdAt;}

    public void setCreatedAt(String createdAt) {this.createdAt = createdAt;}

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