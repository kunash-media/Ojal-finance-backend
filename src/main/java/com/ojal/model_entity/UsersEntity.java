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

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String mobile;

    @Column(name = "alt_mobile")
    private String altMobile;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private String dob;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String pincode;

    @Column(nullable = false)
    private String branch;


    @Column(name="created_at")
    private String createdAt;

    @Column(name="user_roles", nullable = false)
    private String role;

    @Column(name="pan_card",columnDefinition = "LONGBLOB")
    private byte[] panCard;

    @Column(name="aadhar_card",columnDefinition = "LONGBLOB")
    private byte[] aadharCard;

    @Column(name="pass_port_img",columnDefinition = "LONGBLOB")
    private byte[] passPortImg;

    @Column(name="voter_id_img",columnDefinition = "LONGBLOB")
    private byte[] voterIdImg;

    @Column(name="user_signature_img",columnDefinition = "LONGBLOB")
    private byte[] userSignatureImg;

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

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JsonManagedReference
//    @JsonIgnore
//    private List<LoanAccountsEntity> loanAccounts = new ArrayList<>();

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


    public UsersEntity() {}

    public UsersEntity(Long id, String userId, String firstName, String middleName, String lastName,
                       String email, String mobile, String altMobile, String gender, String dob,
                       String address, String pincode, String branch,
                       String createdAt, String role, byte[] panCard, byte[] aadharCard,
                       byte[] passPortImg, byte[] voterIdImg, byte[] userSignatureImg) {
        this.id = id;
        this.userId = userId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.mobile = mobile;
        this.altMobile = altMobile;
        this.gender = gender;
        this.dob = dob;
        this.address = address;
        this.pincode = pincode;
        this.branch = branch;
        this.createdAt = createdAt;
        this.role = role;
        this.panCard = panCard;
        this.aadharCard = aadharCard;
        this.passPortImg = passPortImg;
        this.voterIdImg = voterIdImg;
        this.userSignatureImg = userSignatureImg;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UsersRepository getUsersRepository() {
        return usersRepository;
    }

    public void setUsersRepository(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    // Standard getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAltMobile() {
        return altMobile;
    }

    public void setAltMobile(String altMobile) {
        this.altMobile = altMobile;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }


    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public byte[] getPanCard() {
        return panCard;
    }

    public void setPanCard(byte[] panCard) {
        this.panCard = panCard;
    }

    public byte[] getAadharCard() {
        return aadharCard;
    }

    public void setAadharCard(byte[] aadharCard) {
        this.aadharCard = aadharCard;
    }

    public byte[] getPassPortImg() {
        return passPortImg;
    }

    public void setPassPortImg(byte[] passPortImg) {
        this.passPortImg = passPortImg;
    }

    public byte[] getVoterIdImg() {
        return voterIdImg;
    }

    public void setVoterIdImg(byte[] voterIdImg) {
        this.voterIdImg = voterIdImg;
    }

    public byte[] getUserSignatureImg() {
        return userSignatureImg;
    }

    public void setUserSignatureImg(byte[] userSignatureImg) {
        this.userSignatureImg = userSignatureImg;
    }

    public List<SavingAccountsEntity> getSavingAccounts() {
        return savingAccounts;
    }

    public void setSavingAccounts(List<SavingAccountsEntity> savingAccounts) {
        this.savingAccounts = savingAccounts;
    }

    public List<RdAccountsEntity> getRdAccounts() {
        return rdAccounts;
    }

    public void setRdAccounts(List<RdAccountsEntity> rdAccounts) {
        this.rdAccounts = rdAccounts;
    }

    public List<FdAccountsEntity> getFdAccounts() {
        return fdAccounts;
    }

    public void setFdAccounts(List<FdAccountsEntity> fdAccounts) {
        this.fdAccounts = fdAccounts;
    }

//    public List<LoanAccountsEntity> getLoanAccounts() {
//        return loanAccounts;
//    }

//    public void setLoanAccounts(List<LoanAccountsEntity> loanAccounts) {
//        this.loanAccounts = loanAccounts;
//    }
}

