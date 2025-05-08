package com.ojal.repository;

import com.ojal.model_entity.SavingAccountsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
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
//    boolean existsByUser_UserId(String userId);  // Check if user has any accounts
}
