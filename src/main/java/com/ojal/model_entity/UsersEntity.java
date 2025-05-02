package com.ojal.model_entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ojal.repository.UsersRepository;
import jakarta.persistence.*;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users_table")
public class UsersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", unique = true, nullable = false, updatable = false)
    private String userId;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password; // Should be encrypted in reality

    @Column(name="user_roles", nullable = false)
    private String role;

    // One-to-many relationships with account entities
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    @JsonIgnore
    private List<SavingAccountsEntity> savingAccounts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    @JsonIgnore
    private List<RdAccountsEntity> rdAccounts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    @JsonIgnore
    private List<FdAccountsEntity> fdAccounts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    @JsonIgnore
    private List<LoanAccountsEntity> loanAccounts = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (this.userId == null) {
            this.userId = generate5DigitUserId();
        }
    }

    @Transient
    private UsersRepository usersRepository;

    private String generate5DigitUserId() {
        SecureRandom random = new SecureRandom();
        int maxAttempts = 3;
        int attempt = 0;

        while (attempt < maxAttempts) {
            String candidateId = "U" + (10_000 + random.nextInt(90_000)); // U10000-U99999
            if (usersRepository == null || !usersRepository.existsByUserId(candidateId)) {
                return candidateId;
            }
            attempt++;
        }
        throw new IllegalStateException("Failed to generate unique user ID after " + maxAttempts + " attempts");
    }

    // Helper method to set repository (call this before saving)
    public void setUserRepository(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    // Add methods to manage accounts
    public void addSavingAccount(SavingAccountsEntity account) {
        savingAccounts.add(account);
        account.setUser(this);
    }

    public void addRdAccount(RdAccountsEntity account) {
        rdAccounts.add(account);
        account.setUser(this);
    }

    public void addFdAccount(FdAccountsEntity account) {
        fdAccounts.add(account);
        account.setUser(this);
    }

    public void addLoanAccount(LoanAccountsEntity account) {
        loanAccounts.add(account);
        account.setUser(this);
    }

    // Standard getters and setters
    public String getUserId() {
        return userId;
    }

    public UsersEntity() {
    }

    public UsersEntity(Long id, String userId, String name, String email, String password, String role) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<SavingAccountsEntity> getSavingAccounts() {
        return savingAccounts;
    }

    public List<RdAccountsEntity> getRdAccounts() {
        return rdAccounts;
    }

    public List<FdAccountsEntity> getFdAccounts() {
        return fdAccounts;
    }

    public List<LoanAccountsEntity> getLoanAccounts() {
        return loanAccounts;
    }
}
