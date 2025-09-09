package com.ojal.model_entity.dto.request;

import java.math.BigDecimal;

public class FdAccountUpdateDto {

    private BigDecimal principalAmount;
    private BigDecimal interestRate;
    private Integer tenureMonths;

    // Default constructor
    public FdAccountUpdateDto() {}

    // Constructor with all fields
    public FdAccountUpdateDto(BigDecimal principalAmount, BigDecimal interestRate, Integer tenureMonths) {
        this.principalAmount = principalAmount;
        this.interestRate = interestRate;
        this.tenureMonths = tenureMonths;
    }

    // Getters and Setters
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

    @Override
    public String toString() {
        return "FdAccountUpdateDto{" +
                "principalAmount=" + principalAmount +
                ", interestRate=" + interestRate +
                ", tenureMonths=" + tenureMonths +
                '}';
    }
}