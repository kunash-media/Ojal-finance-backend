package com.ojal.model_entity.dto.response;

import com.ojal.model_entity.UsersEntity;

import java.util.Map;

public class UserDto {

    private String userId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String mobile;
    private String altMobile;
    private String gender;
    private String dob;
    private String address;
    private String pincode;
    private String branch;
    private String role;
    private String createdAt;
    private Map<String, Boolean> documentStatus;

    public UserDto() {
    }

    public UserDto(UsersEntity user) {
        this.userId = user.getUserId();
        this.firstName = user.getFirstName();
        this.middleName = user.getMiddleName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.mobile = user.getMobile();
        this.altMobile = user.getAltMobile();
        this.gender = user.getGender();
        this.dob = user.getDob();
        this.address = user.getAddress();
        this.pincode = user.getPincode();
        this.branch = user.getBranch();
        this.role = user.getRole();
        this.createdAt = user.getCreatedAt();
    }

    // Getters and Setters
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Map<String, Boolean> getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(Map<String, Boolean> documentStatus) {
        this.documentStatus = documentStatus;
    }
}