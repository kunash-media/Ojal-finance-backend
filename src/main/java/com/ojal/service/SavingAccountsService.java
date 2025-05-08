package com.ojal.service;

import com.ojal.model_entity.SavingAccountsEntity;
import com.ojal.model_entity.dto.request.SavingAccountDetailsDto;
import com.ojal.model_entity.dto.request.SavingAccountsDto;

import java.math.BigDecimal;
import java.util.List;

public interface SavingAccountsService {

    SavingAccountsEntity createAccount(String userId, SavingAccountsDto request);

    SavingAccountsEntity findByAccountNumber(String accountNumber);

    // New method to get account details with transactions
    SavingAccountDetailsDto getAccountWithTransactions(String accountNumber);
}
