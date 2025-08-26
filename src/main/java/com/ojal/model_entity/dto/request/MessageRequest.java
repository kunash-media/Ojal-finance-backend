package com.ojal.model_entity.dto.request;

public class MessageRequest {
    private String to;
    private String message;

    // Getters and Setters


    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}