package com.ojal.service;

import com.ojal.model_entity.FdAccountsEntity;
import com.ojal.model_entity.dto.request.FdAccountUpdateDto;
import com.ojal.model_entity.dto.request.FdAccountsDto;

import java.util.List;

public interface FdAccountsService {

    FdAccountsEntity createAccount(String userId, FdAccountsDto request);

    FdAccountsEntity findByAccountNumber(String accountNumber);

    List<FdAccountsEntity> findAllByUserId(String userId);

    List<FdAccountsEntity> findAllAccounts();

    FdAccountsEntity updateAccount(String accountNumber, FdAccountsDto request);

    void deleteAccount(String accountNumber);

    // NEW METHODS MATCHING RD PATTERN
    FdAccountsEntity updateFdAccountPartial(String accountNumber, FdAccountUpdateDto updateRequest);

    void deleteByAccountNumber(String accountNumber);

    int deleteAllByUserId(String userId);
}
