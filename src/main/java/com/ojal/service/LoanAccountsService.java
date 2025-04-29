package com.ojal.service;

import com.ojal.model_entity.LoanAccountsEntity;
import com.ojal.model_entity.dto.request.LoanAccountsDto;

import java.util.List;

public interface LoanAccountsService {
    LoanAccountsEntity createAccount(String userId, LoanAccountsDto request);

    LoanAccountsEntity findByAccountNumber(String accountNumber);

    List<LoanAccountsEntity> findAllByUserId(String userId);

    LoanAccountsEntity processEmiPayment(String accountNumber);
}
