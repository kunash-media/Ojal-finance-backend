package com.ojal.model_entity.dto.request;

import com.ojal.model_entity.RdTransactionEntity;

import java.math.BigDecimal;

public class RdTransactionDTO {

    private Long id;
    private String accountNumber;
    private BigDecimal amount;
    private String payMode;
    private String utrNo;
    private BigDecimal cash;
    private String chequeNumber;
    private String note;
    private BigDecimal balanceAfter;
    private RdTransactionEntity.TransactionStatus status;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
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

    public RdTransactionEntity.TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(RdTransactionEntity.TransactionStatus status) {
        this.status = status;
    }
}