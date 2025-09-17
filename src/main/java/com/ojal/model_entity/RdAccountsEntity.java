package com.ojal.model_entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDate;

@Entity
@Table(name = "rd_accounts_table")
public class RdAccountsEntity extends BaseAccountEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    @JsonBackReference
    private UsersEntity user;

    @Column(nullable = false)
    private BigDecimal depositAmount;

    @Column(name = "interest_rate", nullable = false)
    private BigDecimal interestRate;

    @Column(name = "tenure_months", nullable = false)
    private Integer tenureMonths;

    @Column(name = "maturity_date", nullable = false)
    private LocalDate maturityDate;

    @Column(name = "maturity_amount")
    private BigDecimal maturityAmount;

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "withdrawn_date")
    private LocalDate withdrawnDate;

    @Column(name = "penalty_applied")
    private BigDecimal penaltyApplied = BigDecimal.ZERO;

    @Column(name = "interest_earned")
    private BigDecimal interestEarned = BigDecimal.ZERO;

    @Column(name = "payout_amount")
    private BigDecimal payoutAmount = BigDecimal.ZERO;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BaseAccountEntity.AccountStatus status = BaseAccountEntity.AccountStatus.ACTIVE;


    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (getAccountNumber() == null) {
            setAccountNumber(generateAccountNumber());
        }
    }

    private String generateAccountNumber() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder("RD");
        for (int i = 0; i < 5; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }


    public RdAccountsEntity(){}

    public RdAccountsEntity(UsersEntity user, BigDecimal depositAmount, BigDecimal interestRate,
                            Integer tenureMonths, LocalDate maturityDate,
                            BigDecimal maturityAmount, BigDecimal balance,
                            LocalDate withdrawnDate, BigDecimal penaltyApplied,
                            BigDecimal interestEarned, BigDecimal payoutAmount, AccountStatus status) {
        this.user = user;
        this.depositAmount = depositAmount;
        this.interestRate = interestRate;
        this.tenureMonths = tenureMonths;
        this.maturityDate = maturityDate;
        this.maturityAmount = maturityAmount;
        this.balance = balance;
        this.withdrawnDate = withdrawnDate;
        this.penaltyApplied = penaltyApplied;
        this.interestEarned = interestEarned;
        this.payoutAmount = payoutAmount;
        this.status = status;
    }

    // Getters and setters
    public UsersEntity getUser() {
        return user;
    }

    public void setUser(UsersEntity user) {
        this.user = user;
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

    public LocalDate getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(LocalDate maturityDate) {
        this.maturityDate = maturityDate;
    }

    public BigDecimal getMaturityAmount() {
        return maturityAmount;
    }

    public void setMaturityAmount(BigDecimal maturityAmount) {
        this.maturityAmount = maturityAmount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public LocalDate getWithdrawnDate() {
        return withdrawnDate;
    }

    public void setWithdrawnDate(LocalDate withdrawnDate) {
        this.withdrawnDate = withdrawnDate;
    }

    public BigDecimal getPenaltyApplied() {
        return penaltyApplied;
    }

    public void setPenaltyApplied(BigDecimal penaltyApplied) {
        this.penaltyApplied = penaltyApplied;
    }

    public BigDecimal getInterestEarned() {
        return interestEarned;
    }

    public void setInterestEarned(BigDecimal interestEarned) {
        this.interestEarned = interestEarned;
    }

    public BigDecimal getPayoutAmount() {
        return payoutAmount;
    }

    public void setPayoutAmount(BigDecimal payoutAmount) {
        this.payoutAmount = payoutAmount;
    }

    @Override
    public AccountStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(AccountStatus status) {
        this.status = status;
    }
}
