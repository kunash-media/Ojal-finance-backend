package com.ojal.service;

import com.ojal.model_entity.RdAccountsEntity;
import com.ojal.model_entity.UsersEntity;
import com.ojal.model_entity.dto.request.RdAccountsDto;

import java.util.List;

public interface RdAccountsService {

    UsersEntity createAccount(String userId, RdAccountsDto request);

    RdAccountsEntity findByAccountNumber(String accountNumber);

    List<RdAccountsEntity> findAllByUserId(String userId);

    RdAccountsEntity processMonthlyDeposit(String accountNumber);
    // Additional RD account management methods
}
