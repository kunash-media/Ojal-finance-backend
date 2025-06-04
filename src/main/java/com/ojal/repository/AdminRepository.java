package com.ojal.repository;

import com.ojal.model_entity.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<AdminEntity, String> {

    Optional<AdminEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<AdminEntity> findByPhoneOrAltPhone(String phone, String altPhone);

    Optional<AdminEntity> findByUsername(String username);

    @Query("SELECT DISTINCT a.branchName FROM AdminEntity a WHERE a.branchName IS NOT NULL")
    List<String> findAllDistinctBranchNames();
}