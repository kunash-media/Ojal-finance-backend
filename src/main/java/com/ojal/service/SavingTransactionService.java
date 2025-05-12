package com.ojal.service;


import com.ojal.model_entity.SavingTransactionEntity;
import com.ojal.model_entity.dto.request.SavingTransactionDto;

import java.util.List;

public interface SavingTransactionService {

    SavingTransactionEntity createTransaction(String accountNumber, SavingTransactionDto transactionDto);

    List<SavingTransactionEntity> findByAccountNumber(String accountNumber);
}