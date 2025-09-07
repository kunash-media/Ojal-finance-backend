package com.ojal.model_entity.dto.request;

import java.math.BigDecimal;

public class RdAccountUpdateDto {

    private BigDecimal depositAmount;
    private BigDecimal interestRate;
    private Integer tenureMonths;

    // Default constructor
    public RdAccountUpdateDto() {}

    // Constructor with all fields
    public RdAccountUpdateDto(BigDecimal depositAmount, BigDecimal interestRate, Integer tenureMonths) {
        this.depositAmount = depositAmount;
        this.interestRate = interestRate;
        this.tenureMonths = tenureMonths;
    }

    // Getters and Setters
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

    @Override
    public String toString() {
        return "RdAccountUpdateDto{" +
                "depositAmount=" + depositAmount +
                ", interestRate=" + interestRate +
                ", tenureMonths=" + tenureMonths +
                '}';
    }
}