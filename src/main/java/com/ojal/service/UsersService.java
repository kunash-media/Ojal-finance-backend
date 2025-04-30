package com.ojal.service;

import com.ojal.model_entity.UsersEntity;
import com.ojal.model_entity.dto.request.UserRegistrationDto;

import java.util.List;

public interface UsersService {

    UsersEntity createUser(UserRegistrationDto request);

    UsersEntity findByUserId(String userId);

    boolean existsByUserId(String userId);

    UsersEntity findByEmail(String email);

    boolean existsByEmail(String email);
    // Additional user management methods

    List<UsersEntity> getAllUsers();
}
