package com.ojal.repository;

import com.ojal.model_entity.FdAccountsEntity;
import com.ojal.model_entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FdAccountsRepository extends JpaRepository<FdAccountsEntity,Long> {

    Optional<FdAccountsEntity> findByAccountNumber(String accountNumber);

    // Query by User entity (requires fetching user first)
    List<FdAccountsEntity> findByUser(UsersEntity user);

}
