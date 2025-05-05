package com.ojal.model_entity.dto.response;

import java.util.Map;

public class UserRegistrationResponseDto {
    private String userId;
    private String name;
    private String email;
    private String role;
    private Map<String, Boolean> documentStatus;

    // Default constructor
    public UserRegistrationResponseDto() {
    }

    // Constructor
    public UserRegistrationResponseDto(String userId, String name, String email, String role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    // Getters and setters
    public String getUserId() {
        return userId;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Map<String, Boolean> getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(Map<String, Boolean> documentStatus) {
        this.documentStatus = documentStatus;
    }
}