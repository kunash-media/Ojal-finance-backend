package com.ojal.repository;

import com.ojal.model_entity.SavingAccountsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavingAccountsRepository extends JpaRepository<SavingAccountsEntity,Long> {

    // Correct query for custom userId (String)
    List<SavingAccountsEntity> findByUser_userId(String userId);

    // Query by JPA-generated user ID (Long) - keep if needed elsewhere
    List<SavingAccountsEntity> findByUser_Id(Long id);

    // Account operations
    Optional<SavingAccountsEntity> findByAccountNumber(String accountNumber);
    boolean existsByAccountNumber(String accountNumber);

    // Additional useful queries
//    long countByUser_UserId(String userId);  // Count accounts by custom userId
    boolean existsByUser_UserId(String userId);  // Check if user has any accounts

    /**
     * Find saving account by user's custom userId
     * @param userId the custom user ID
     * @return Optional<SavingAccountsEntity>
     */
    Optional<SavingAccountsEntity> findByUser_UserId(String userId);

    /**
     * Find all saving accounts by user's branch
     * @param branch the branch name
     * @return List<SavingAccountsEntity>
     */
    List<SavingAccountsEntity> findByUser_Branch(String branch);

    /**
     * Delete saving account by user's custom userId
     * @param userId the custom user ID
     * @return number of deleted records
     */
    @Modifying
    @Query("DELETE FROM SavingAccountsEntity s WHERE s.user.userId = :userId")
    int deleteByUser_UserId(@Param("userId") String userId);

}
