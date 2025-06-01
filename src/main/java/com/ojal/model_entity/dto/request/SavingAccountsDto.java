package com.ojal.model_entity.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class SavingAccountsDto {

    @JsonProperty("interestRate")
    private BigDecimal interestRate;

    @JsonProperty("minimumBalance")
    private BigDecimal minimumBalance;

    @JsonProperty("initialDeposit")
    private BigDecimal initialDeposit;

    // Getters and setters
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
}