package com.ojal.service.service_impl;

import com.ojal.model_entity.UsersEntity;
import com.ojal.model_entity.dto.request.UserRegistrationDto;
import com.ojal.repository.UsersRepository;
import com.ojal.service.UsersService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;

    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    @Transactional
    public UsersEntity createUser(UserRegistrationDto request) {
        // Check if email already exists
        if (usersRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered: " + request.getEmail());
        }

        UsersEntity user = new UsersEntity();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // Should be encrypted in production
        user.setUserRepository(usersRepository); // Set repository for ID generation

        return usersRepository.save(user);
    }

    @Override
    public UsersEntity findByUserId(String userId) {
        return usersRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
    }

    @Override
    public boolean existsByUserId(String userId) {
        return usersRepository.existsByUserId(userId);
    }

    @Override
    public UsersEntity findByEmail(String email) {
        return usersRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
    }

    @Override
    public boolean existsByEmail(String email) {
        return usersRepository.existsByEmail(email);
    }
}
