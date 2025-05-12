package com.ojal.model_entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "rd_transactions_table")
public class RdTransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "created_at", nullable = false, updatable = false)
    private String createdAt;

    @Column(name = "last_updated")
    private String lastUpdated;

    @Column(name = "pay_mode")
    private String payMode;

    @Column(name = "utr_no")
    private String utrNo;

    @Column(name = "cash_amount")
    private BigDecimal cash;

    @Column(name = "cheque_number")
    private String chequeNumber;

    @Column(nullable = true)
    private String note;



    @Column(name = "balance_after")
    private BigDecimal balanceAfter;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus status = TransactionStatus.COMPLETED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_number", referencedColumnName = "account_number")
    @JsonBackReference
    private RdAccountsEntity rdAccount;

    // Enum for transaction status
    public enum TransactionStatus {
        COMPLETED, UNCOMPLETED
    }

    @PrePersist
    protected void onCreate() {
        String formattedTime = getCurrentFormattedTime();
        this.createdAt = formattedTime;
        this.lastUpdated = formattedTime;
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastUpdated = getCurrentFormattedTime();
    }

    private String getCurrentFormattedTime() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
        return now.format(formatter);
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public String getUtrNo() {
        return utrNo;
    }

    public void setUtrNo(String utrNo) {
        this.utrNo = utrNo;
    }

    public BigDecimal getCash() {
        return cash;
    }

    public void setCash(BigDecimal cash) {
        this.cash = cash;
    }

    public String getChequeNumber() {
        return chequeNumber;
    }

    public void setChequeNumber(String chequeNumber) {
        this.chequeNumber = chequeNumber;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(BigDecimal balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public RdAccountsEntity getRdAccount() {
        return rdAccount;
    }

    public void setRdAccount(RdAccountsEntity rdAccount) {
        this.rdAccount = rdAccount;
    }
}