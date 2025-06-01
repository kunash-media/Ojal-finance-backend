package com.ojal.model_entity.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class SavingAccountUpdateDto {

    @JsonProperty("interestRate")
    private BigDecimal interestRate;

    @JsonProperty("minimumBalance")
    private BigDecimal minimumBalance;

    @JsonProperty("initialDeposit")  // Add this field
    private BigDecimal initialDeposit;

    // Constructors
    public SavingAccountUpdateDto() {
    }

    public SavingAccountUpdateDto(BigDecimal interestRate, BigDecimal minimumBalance, BigDecimal initialDeposit) {
        this.interestRate = interestRate;
        this.minimumBalance = minimumBalance;
        this.initialDeposit = initialDeposit;
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

    public BigDecimal getInitialDeposit() {
        return initialDeposit;
    }

    public void setInitialDeposit(BigDecimal initialDeposit) {
        this.initialDeposit = initialDeposit;
    }

    @Override
    public String toString() {
        return "SavingAccountUpdateDto{" +
                "interestRate=" + interestRate +
                ", minimumBalance=" + minimumBalance +
                '}';
    }
}