package com.ojal.repository;

import com.ojal.model_entity.LoanAccountsEntity;
import com.ojal.model_entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanAccountsRepository extends JpaRepository<LoanAccountsEntity, String> {
    Optional<LoanAccountsEntity> findByAccountNumber(String accountNumber);
    Optional<LoanAccountsEntity> findByApplicationNo(String applicationNo);
}
