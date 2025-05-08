package com.ojal.model_entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions_table")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_number", referencedColumnName = "account_number", nullable = false)
    @JsonBackReference
    private SavingAccountsEntity savingAccount;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "pay_mode")
    private String payMode;

    @Column(name = "utr_no")
    private String utrNo;

    private String cash;

    @Column(name = "cheque_number")
    private String chequeNumber;

    private String note;

    @Column(name = "balance_after")
    private BigDecimal balanceAfter;

    public TransactionEntity() {
    }

    public TransactionEntity(Long id, SavingAccountsEntity savingAccount, LocalDateTime date,
                             BigDecimal amount, String payMode, String utrNo,
                             String cash, String chequeNumber, String note,
                             BigDecimal balanceAfter) {
        this.id = id;
        this.savingAccount = savingAccount;
        this.date = date;
        this.amount = amount;
        this.payMode = payMode;
        this.utrNo = utrNo;
        this.cash = cash;
        this.chequeNumber = chequeNumber;
        this.note = note;
        this.balanceAfter = balanceAfter;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SavingAccountsEntity getSavingAccount() {
        return savingAccount;
    }

    public void setSavingAccount(SavingAccountsEntity savingAccount) {
        this.savingAccount = savingAccount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
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
}