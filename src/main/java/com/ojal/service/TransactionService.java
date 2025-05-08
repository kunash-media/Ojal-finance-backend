package com.ojal.service;


import com.ojal.model_entity.TransactionEntity;
import com.ojal.model_entity.dto.request.TransactionDto;

import java.util.List;

public interface TransactionService {

    TransactionEntity createTransaction(String accountNumber, TransactionDto transactionDto);

    List<TransactionEntity> findByAccountNumber(String accountNumber);
}