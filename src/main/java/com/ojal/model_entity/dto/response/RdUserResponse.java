package com.ojal.model_entity.dto.response;

import com.ojal.controller.AccountsController;

import java.util.List;

public class RdUserResponse {

    private String userId;
    private String name;  // Add other user fields you need
    private List<AccountsController.AccountResponse> rdAccounts;

    // Constructors
    public RdUserResponse() {}

    public RdUserResponse(String userId, String name, List<AccountsController.AccountResponse> rdAccounts) {
        this.userId = userId;
        this.name = name;
        this.rdAccounts = rdAccounts;
    }

    // Getters and Setters
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

    public List<AccountsController.AccountResponse> getRdAccounts() {
        return rdAccounts;
    }

    public void setRdAccounts(List<AccountsController.AccountResponse> rdAccounts) {
        this.rdAccounts = rdAccounts;
    }
}
