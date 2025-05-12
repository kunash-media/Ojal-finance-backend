package com.ojal.model_entity.dto.response;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class RdAccountResponseDTO {

    private Long id;
    private String name;
    private String accountNumber;
    private String createdAt;
    private String accountType = "RD_AC";
    private BigDecimal depositAmount;
    private BigDecimal interestRate;
    private Integer tenureMonths;
    private LocalDate maturityDate;
    private BigDecimal maturityAmount;
    private String status;
    private List<RdTransactionResponseDTO> transactionData;

    // Inner class for transaction response
    public static class RdTransactionResponseDTO {
        private String date;
        private BigDecimal amount;
        private String payMode;
        private String utrNo;
        private BigDecimal cash;
        private String chequeNumber;
        private String note;
        private BigDecimal balanceAfter;
        private String status;

        // Getters and Setters
        public String getDate() {
            return date;
        }

        public void setDate(String date) {
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    // Getters and Setters for main RD account response
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getAccountType() {
        return accountType;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<RdTransactionResponseDTO> getTransactionData() {
        return transactionData;
    }

    public void setTransactionData(List<RdTransactionResponseDTO> transactionData) {
        this.transactionData = transactionData;
    }
}