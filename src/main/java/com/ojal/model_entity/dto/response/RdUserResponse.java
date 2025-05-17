package com.ojal.model_entity.dto.response;

import com.ojal.controller.AccountsController;
import com.ojal.controller.RdAccountsController;

import java.util.List;

public class RdUserResponse {

    private String userId;
    private String name;
    private List<RdAccountsController.AccountResponse> rdAccounts;

    // Constructors
    public RdUserResponse() {}

    public RdUserResponse(String userId, String name, List<RdAccountsController.AccountResponse> rdAccounts) {
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

    public List<RdAccountsController.AccountResponse> getRdAccounts() {
        return rdAccounts;
    }

    public void setRdAccounts(List<RdAccountsController.AccountResponse> rdAccounts) {
        this.rdAccounts = rdAccounts;
    }
}