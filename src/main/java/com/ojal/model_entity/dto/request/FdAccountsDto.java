package com.ojal.model_entity.dto.request;

import java.math.BigDecimal;

public class FdAccountsDto {

    private BigDecimal principalAmount;
    private BigDecimal interestRate;
    private Integer tenureMonths;

    // Getters and setters
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

}
