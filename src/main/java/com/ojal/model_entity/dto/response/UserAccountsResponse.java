package com.ojal.model_entity.dto.response;

import com.ojal.model_entity.*;

import java.util.List;

public class UserAccountsResponse {

    private String userId;
    private String userName;
    private String userRole;
    private List<SavingAccountsEntity> savingAccounts;
    private List<RdAccountsEntity> rdAccounts;
    private List<FdAccountsEntity> fdAccounts;
//    private List<LoanAccountsEntity> loanAccounts;

    // Constructor to build from user entity
    public UserAccountsResponse(UsersEntity user) {
        this.userId = user.getUserId();
        this.userName = user.getFirstName();
        this.userRole = user.getRole();
        this.savingAccounts = user.getSavingAccounts();
        this.rdAccounts = user.getRdAccounts();
        this.fdAccounts = user.getFdAccounts();
//        this.loanAccounts = user.getLoanAccounts();
    }

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

