package com.ojal.model_entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@MappedSuperclass
public class BaseAccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", unique = true, nullable = false, updatable = false)
    private String accountNumber;

    @Column(name = "created_date", nullable = false, updatable = false)
    private String createdAt;

    @Column(name = "last_updated")
    private String lastUpdated;

    @Column(name = "account_status")
    @Enumerated(EnumType.STRING)
    private AccountStatus status = AccountStatus.ACTIVE;


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

    // Common getters and setters
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

    public String getCreatedAt() {
        return createdAt;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    // Enum for account status
    public enum AccountStatus {
        ACTIVE, INACTIVE, CLOSED
    }
}
