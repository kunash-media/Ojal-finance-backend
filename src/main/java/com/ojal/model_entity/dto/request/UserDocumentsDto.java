package com.ojal.model_entity.dto.request;

public class UserDocumentsDto {

    private String documentType; // Type of document: PAN, AADHAR, PASSPORT, VOTER_ID

    private String userId;
    private Boolean uploaded;
    private String message;

    public UserDocumentsDto() {
    }

    // Response constructor
    public UserDocumentsDto(String userId, Boolean uploaded, String message) {
        this.userId = userId;
        this.uploaded = uploaded;
        this.message = message;
    }

    // Getters and setters
    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getUploaded() {
        return uploaded;
    }

    public void setUploaded(Boolean uploaded) {
        this.uploaded = uploaded;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
