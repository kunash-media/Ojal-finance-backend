package com.ojal.repository;

import com.ojal.model_entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<UsersEntity, Long> {

    boolean existsByEmail(String email);
    boolean existsByUserId(String userId);

    Optional<UsersEntity> findByUserId(String userId);
    Optional<UsersEntity> findByEmail(String email);

    
}
