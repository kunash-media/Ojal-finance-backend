package com.ojal.service;

import com.ojal.model_entity.UsersEntity;
import com.ojal.model_entity.Users_Dto.UserRegistrationDto;
import org.springframework.http.ResponseEntity;

public interface UsersService {

    UsersEntity createUser(UserRegistrationDto request);

    UsersEntity findByUserId(String userId);

    boolean existsByUserId(String userId);

    UsersEntity findByEmail(String email);

    boolean existsByEmail(String email);
    // Additional user management methods
}
