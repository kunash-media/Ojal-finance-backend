package com.ojal.model_entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "fd_transactions_table")
public class FdTransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Foreign key relationship with FD Account using account number
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_number", referencedColumnName = "account_number", nullable = false)
    @JsonBackReference
    private FdAccountsEntity fdAccount;

    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "created_at", nullable = false, updatable = false)
    private String createdAt;

    @Column(name = "pay_mode", nullable = false)
    private String payMode;

    @Column(name = "utr_no")
    private String utrNo;

    @Column(name = "cash", precision = 15, scale = 2)
    private BigDecimal cash;

    @Column(name = "cheque_number")
    private String chequeNumber;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus status = TransactionStatus.SUCCESS;

    @Column(name = "note")
    private String note;

    @Column(name = "balance_after", nullable = false, precision = 15, scale = 2)
    private BigDecimal balanceAfter;

    @Column(name = "transaction_id", unique = true, nullable = false, updatable = false)
    private String transactionId;

    // Lifecycle callback to set creation timestamp and generate transaction ID
    @PrePersist
    protected void onCreate() {
        this.createdAt = getCurrentFormattedTime();
        if (this.transactionId == null) {
            this.transactionId = generateTransactionId();
        }
    }

    /**
     * Generates current formatted timestamp
     * @return formatted timestamp string
     */
    private String getCurrentFormattedTime() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
        return now.format(formatter);
    }

    /**
     * Generates unique transaction ID for FD transactions
     * @return unique transaction ID starting with "FDT"
     */
    private String generateTransactionId() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder("FDT");
        for (int i = 0; i < 8; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    // Constructors
    public FdTransactionEntity() {}


    public FdTransactionEntity(FdAccountsEntity fdAccount, BigDecimal amount, String payMode,
                               String utrNo, BigDecimal cash, String chequeNumber, String note,
                               BigDecimal balanceAfter) {
        this.fdAccount = fdAccount;
        this.amount = amount;
        this.payMode = payMode;
        this.utrNo = utrNo;
        this.cash = cash;
        this.chequeNumber = chequeNumber;
        this.note = note;
        this.balanceAfter = balanceAfter;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FdAccountsEntity getFdAccount() {
        return fdAccount;
    }

    public void setFdAccount(FdAccountsEntity fdAccount) {
        this.fdAccount = fdAccount;
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

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
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

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    // Enums for transaction properties
    public enum PayMode {
        CASH, CHEQUE, IMPS, NEFT, RTGS, UPI, ONLINE_TRANSFER, WITHDRAWAL
    }

    public enum TransactionStatus {
        PENDING, SUCCESS, FAILED, CANCELLED
    }
}