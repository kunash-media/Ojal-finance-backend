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

   // List<SavingTransactionEntity> findBySavingAccount_AccountNumberOrderByCreatedAtDesc(String accountNumber);

    @Query("SELECT t FROM SavingTransactionEntity t JOIN FETCH t.savingAccount WHERE t.savingAccount.accountNumber = :accountNumber ORDER BY t.createdAt DESC")
    List<SavingTransactionEntity> findBySavingAccount_AccountNumberOrderByCreatedAtDesc(@Param("accountNumber") String accountNumber);


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

    // Add this method to your SavingTransactionRepository interface
    @Query(value = "SELECT st.id, st.created_at, st.daily_amount, st.pay_mode, st.utr_no, " +
            "st.cash, st.cheque_number, st.note, st.balance_after " +
            "FROM saving_transactions st " +
            "INNER JOIN saving_accounts_table sa ON st.account_number = sa.account_number " +
            "WHERE sa.account_number = :accountNumber " +
            "ORDER BY st.created_at DESC",
            nativeQuery = true)
    List<Object[]> findTransactionRawDataByAccountNumber(@Param("accountNumber") String accountNumber);
}