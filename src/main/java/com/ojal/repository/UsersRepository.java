package com.ojal.repository;

import com.ojal.model_entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<UsersEntity, Long> {

    boolean existsByEmail(String email);
    boolean existsByUserId(String userId);
    boolean existsByMobile(String mobile);

    UsersEntity findByUserId(String userId);
    Optional<UsersEntity> findByEmail(String email);

    // For delete operation
    void deleteByUserId(String userId);

    // For get all users (admin functionality)
    List<UsersEntity> findAll();

    // Find users by role
    List<UsersEntity> findByRole(String role);
}
