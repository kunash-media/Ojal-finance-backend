package com.ojal.model_entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDate;

@Entity
@Table(name = "loan_accounts_table")
public class LoanAccountsEntity extends BaseAccountEntity {


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    @JsonBackReference
    private UsersEntity user;

    @Column(name = "loan_amount", nullable = false)
    private BigDecimal loanAmount;

    @Column(name = "interest_rate", nullable = false)
    private BigDecimal interestRate;

    @Column(name = "tenure_months", nullable = false)
    private Integer tenureMonths;

    @Column(name = "emi_amount")
    private BigDecimal emiAmount;

    @Column(name = "disbursement_date")
    private LocalDate disbursementDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "outstanding_amount")
    private BigDecimal outstandingAmount;

    @Column(name = "loan_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private LoanType loanType;

    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (getAccountNumber() == null) {
            setAccountNumber(generateAccountNumber());
        }
        if (disbursementDate != null) {
            outstandingAmount = loanAmount;
        }
    }

    private String generateAccountNumber() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder("LN");
        for (int i = 0; i < 5; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    // Loan type enum
    public enum LoanType {
        PERSONAL, HOME, EDUCATION, VEHICLE, BUSINESS
    }

    // Getters and setters
    public UsersEntity getUser() {
        return user;
    }

    public void setUser(UsersEntity user) {
        this.user = user;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
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

    public BigDecimal getEmiAmount() {
        return emiAmount;
    }

    public void setEmiAmount(BigDecimal emiAmount) {
        this.emiAmount = emiAmount;
    }

    public LocalDate getDisbursementDate() {
        return disbursementDate;
    }

    public void setDisbursementDate(LocalDate disbursementDate) {
        this.disbursementDate = disbursementDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getOutstandingAmount() {
        return outstandingAmount;
    }

    public void setOutstandingAmount(BigDecimal outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    public LoanType getLoanType() {
        return loanType;
    }

    public void setLoanType(LoanType loanType) {
        this.loanType = loanType;
    }
}