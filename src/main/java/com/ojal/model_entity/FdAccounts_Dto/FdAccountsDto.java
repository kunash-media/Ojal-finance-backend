package com.ojal.model_entity.FdAccounts_Dto;

import java.math.BigDecimal;

public class FdAccountsDto {

    private BigDecimal principalAmount;
    private BigDecimal interestRate;
    private Integer tenureDays;
    private Boolean autoRenewal;

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

    public Integer getTenureDays() {
        return tenureDays;
    }

    public void setTenureDays(Integer tenureDays) {
        this.tenureDays = tenureDays;
    }

    public Boolean getAutoRenewal() {
        return autoRenewal;
    }

    public void setAutoRenewal(Boolean autoRenewal) {
        this.autoRenewal = autoRenewal;
    }
}
