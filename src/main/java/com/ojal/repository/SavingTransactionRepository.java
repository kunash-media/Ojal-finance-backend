package com.ojal.repository;

import com.ojal.model_entity.SavingTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavingTransactionRepository extends JpaRepository<SavingTransactionEntity, Long> {
    List<SavingTransactionEntity> findBySavingAccount_AccountNumberOrderByCreatedAtDesc(String accountNumber);

    // ADD these new methods:

    /**
     * Delete all transactions for a specific account number
     */
    @Modifying
    @Query("DELETE FROM SavingTransactionEntity st WHERE st.savingAccount.accountNumber = :accountNumber")
    void deleteByAccountNumber(@Param("accountNumber") String accountNumber);

    /**
     * Delete all transactions for accounts belonging to a specific user
     */
    @Modifying
    @Query("DELETE FROM SavingTransactionEntity st WHERE st.savingAccount.user.userId = :userId")
    void deleteByUserUserId(@Param("userId") String userId);

    /**
     * Find transactions by user ID (for validation purposes)
     */
    @Query("SELECT st FROM SavingTransactionEntity st WHERE st.savingAccount.user.userId = :userId")
    List<SavingTransactionEntity> findByUserUserId(@Param("userId") String userId);
}