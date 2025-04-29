package com.ojal.model_entity.dto.request;

import java.math.BigDecimal;

public class RdAccountsDto {

    private BigDecimal depositAmount;
    private BigDecimal interestRate;
    private Integer tenureMonths;

    // Getters and setters
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
}