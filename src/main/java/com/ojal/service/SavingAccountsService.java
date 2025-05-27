package com.ojal.service;

import com.ojal.model_entity.SavingAccountsEntity;
import com.ojal.model_entity.dto.request.SavingAccountDetailsDto;
import com.ojal.model_entity.dto.request.SavingAccountUpdateDto;
import com.ojal.model_entity.dto.request.SavingAccountsDto;
import java.util.List;

public interface SavingAccountsService {

    SavingAccountsEntity createAccount(String userId, SavingAccountsDto request);

    SavingAccountsEntity findByAccountNumber(String accountNumber);

    // New method to get account details with transactions
    SavingAccountDetailsDto getAccountWithTransactions(String accountNumber);

    /**
     * Get saving account by user ID
     * @param userId the user ID to search for
     * @return SavingAccountsEntity if found
     */
    SavingAccountsEntity getAccountByUserId(String userId);

    /**
     * Update saving account by user ID (excluding account number)
     * @param userId the user ID
     * @param updateDto the update data
     * @return updated SavingAccountsEntity
     */
    SavingAccountsEntity updateAccountByUserId(String userId, SavingAccountUpdateDto updateDto);



    /**
     * Get all saving accounts by branch name
     * @param branchName the branch name to filter by
     * @return List of SavingAccountsEntity
     */
    List<SavingAccountsEntity> getAllAccountsByBranch(String branchName);

    /**
     * Delete saving account by user ID
     * @param userId the user ID
     * @return success message
     */
    String deleteAccountByUserId(String userId);
}
