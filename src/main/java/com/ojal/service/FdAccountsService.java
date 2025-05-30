package com.ojal.service;

import com.ojal.model_entity.FdAccountsEntity;
import com.ojal.model_entity.dto.request.FdAccountsDto;

import java.util.List;

public interface FdAccountsService {

    FdAccountsEntity createAccount(String userId, FdAccountsDto request);

    FdAccountsEntity findByAccountNumber(String accountNumber);

    List<FdAccountsEntity> findAllByUserId(String userId);

    List<FdAccountsEntity> findAllAccounts();

    FdAccountsEntity updateAccount(String accountNumber, FdAccountsDto request);

    void deleteAccount(String accountNumber);
}
