package com.ojal.model_entity.dto.request;

import java.math.BigDecimal;

public class SavingAccountUpdateDto {

    private BigDecimal interestRate;
    private BigDecimal minimumBalance;

    // Constructors
    public SavingAccountUpdateDto() {
    }

    public SavingAccountUpdateDto(BigDecimal interestRate, BigDecimal minimumBalance) {
        this.interestRate = interestRate;
        this.minimumBalance = minimumBalance;
    }

    // Getters and Setters
    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public BigDecimal getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(BigDecimal minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    @Override
    public String toString() {
        return "SavingAccountUpdateDto{" +
                "interestRate=" + interestRate +
                ", minimumBalance=" + minimumBalance +
                '}';
    }
}