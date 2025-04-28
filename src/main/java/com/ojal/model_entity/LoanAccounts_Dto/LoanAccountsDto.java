package com.ojal.model_entity.LoanAccounts_Dto;

import com.ojal.model_entity.LoanAccountsEntity;

import java.math.BigDecimal;

public class LoanAccountsDto{

    private BigDecimal loanAmount;
    private BigDecimal interestRate;
    private Integer tenureMonths;
    private LoanAccountsEntity.LoanType loanType;

    // Getters and setters
    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
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

    public LoanAccountsEntity.LoanType getLoanType() {
        return loanType;
    }

    public void setLoanType(LoanAccountsEntity.LoanType loanType) {
        this.loanType = loanType;
    }
}