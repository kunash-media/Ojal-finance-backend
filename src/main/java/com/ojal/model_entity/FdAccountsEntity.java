package com.ojal.model_entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "fd_accounts_table")
public class FdAccountsEntity extends BaseAccountEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    @JsonBackReference
    private UsersEntity user;

    @OneToMany(mappedBy = "fdAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference
    @JsonIgnore
    private List<FdTransactionEntity> fdTransactions = new ArrayList<>();

    @Column(name = "principal_amount", nullable = false)
    private BigDecimal principalAmount;

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
    private BigDecimal penaltyApplied;

    @Column(name = "payout_amount")
    private BigDecimal payoutAmount;

    @Column(name = "is_withdrawn")
    private Boolean isWithdrawn = false;

    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (getAccountNumber() == null) {
            setAccountNumber(generateAccountNumber());
        }
    }

    // FD Account Number Generator.
    private String generateAccountNumber() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder("FD");
        for (int i = 0; i < 5; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public FdAccountsEntity(){}

    public FdAccountsEntity(UsersEntity user, List<FdTransactionEntity> fdTransactions,
                            BigDecimal principalAmount, BigDecimal interestRate, Integer tenureMonths,
                            LocalDate maturityDate, BigDecimal maturityAmount, BigDecimal balance,
                            LocalDate withdrawnDate, BigDecimal penaltyApplied, BigDecimal payoutAmount,
                            Boolean isWithdrawn) {
        this.user = user;
        this.fdTransactions = fdTransactions;
        this.principalAmount = principalAmount;
        this.interestRate = interestRate;
        this.tenureMonths = tenureMonths;
        this.maturityDate = maturityDate;
        this.maturityAmount = maturityAmount;
        this.balance = balance;
        this.withdrawnDate = withdrawnDate;
        this.penaltyApplied = penaltyApplied;
        this.payoutAmount = payoutAmount;
        this.isWithdrawn = isWithdrawn;
    }

    // Getters and setters
    public UsersEntity getUser() {
        return user;
    }

    public void setUser(UsersEntity user) {
        this.user = user;
    }

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

    public BigDecimal getPayoutAmount() {
        return payoutAmount;
    }

    public void setPayoutAmount(BigDecimal payoutAmount) {
        this.payoutAmount = payoutAmount;
    }


    public Boolean getIsWithdrawn() {
        return isWithdrawn;
    }

    public void setIsWithdrawn(Boolean withdrawn) {
        isWithdrawn = withdrawn;
    }

    public List<FdTransactionEntity> getFdTransactions() {
        return fdTransactions;
    }

    public void setFdTransactions(List<FdTransactionEntity> fdTransactions) {
        this.fdTransactions = fdTransactions;
    }
}
