package com.ojal.repository;

import com.ojal.model_entity.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<AdminEntity, String> {

    Optional<AdminEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<AdminEntity> findByPhoneOrAltPhone(String phone, String altPhone);

    Optional<AdminEntity> findByUsername(String username);
}