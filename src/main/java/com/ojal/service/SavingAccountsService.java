package com.ojal.service;

import com.ojal.model_entity.SavingAccountsEntity;
import com.ojal.model_entity.dto.request.SavingAccountsDto;

import java.math.BigDecimal;
import java.util.List;

public interface SavingAccountsService {

    SavingAccountsEntity createAccount(String userId, SavingAccountsDto request);

    SavingAccountsEntity findByAccountNumber(String accountNumber);

    List<SavingAccountsEntity> findAllByUserId(String userId);

    SavingAccountsEntity updateBalance(String accountNumber, BigDecimal amount);
    // Additional saving account management methods
}
