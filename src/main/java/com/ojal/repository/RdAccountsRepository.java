package com.ojal.repository;

import com.ojal.model_entity.RdAccountsEntity;
import com.ojal.model_entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RdAccountsRepository extends JpaRepository<RdAccountsEntity, Long> {

    // For findByAccountNumber()
    Optional<RdAccountsEntity> findByAccountNumber(String accountNumber);

    List<RdAccountsEntity> findByUser(UsersEntity user);

    boolean existsByAccountNumber(String accountNumber);
}
