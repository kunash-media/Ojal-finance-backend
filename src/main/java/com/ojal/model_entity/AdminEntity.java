package com.ojal.model_entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;


import jakarta.persistence.*;

@Entity
@Table(name = "admin_table")
public class AdminEntity {

    @Id
    @Column(name = "admin_id")
    private String adminId;  // Format: ADM123

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "alt_phone")
    private String altPhone;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "gender")
    private String gender;

    @Column(name = "branch_name")
    private String branchName;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    private String role;

    public AdminEntity() {}

    // Method to generate random adminId
    @PrePersist
    public void generateAdminId() {
        if (this.adminId == null) {
            // Generate a random number between 100 and 999
            int randomNum = 100 + (int)(Math.random() * 900);
            this.adminId = "ADM" + randomNum;
        }
    }

    public AdminEntity(String adminId, String fullName, String phone, String altPhone,
                       String email, String gender,
                       String branchName, String username,
                       String password, String role) {
        this.adminId = adminId;
        this.fullName = fullName;
        this.phone = phone;
        this.altPhone = altPhone;
        this.email = email;
        this.gender = gender;
        this.branchName = branchName;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAltPhone() {
        return altPhone;
    }

    public void setAltPhone(String altPhone) {
        this.altPhone = altPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
