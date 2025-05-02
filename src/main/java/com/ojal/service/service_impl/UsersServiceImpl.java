package com.ojal.service.service_impl;

import com.ojal.bcrypt.BcryptEncoderConfig;
import com.ojal.model_entity.UsersEntity;
import com.ojal.model_entity.dto.request.UserRegistrationDto;
import com.ojal.repository.UsersRepository;
import com.ojal.service.UsersService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final BcryptEncoderConfig passwordEncoder;

    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository,
                            BcryptEncoderConfig passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder =   passwordEncoder;
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

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(encodedPassword);
        user.setRole("ROLE_USER");
        user.setUserRepository(usersRepository);

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

    @Override
    public List<UsersEntity> getAllUsers(){
        List<UsersEntity> users =  usersRepository.findAll();
       return users;
    }

    @Override
    @Transactional
    public void updateUser(String userId, UserRegistrationDto userRegistrationDto) {

        UsersEntity user = usersRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        // Update name if provided
        if (userRegistrationDto.getName() != null && !userRegistrationDto.getName().isEmpty()) {
            user.setName(userRegistrationDto.getName());
        }

        // Update role if provided
        if (userRegistrationDto.getRole() != null && !userRegistrationDto.getRole().isEmpty()) {
            user.setRole(userRegistrationDto.getRole());
        }

        // Update password if provided
        if (userRegistrationDto.getPassword() != null && !userRegistrationDto.getPassword().isEmpty()) {
            // Encrypt the password before saving
            String encryptedPassword = passwordEncoder.encode(userRegistrationDto.getPassword());
            user.setPassword(encryptedPassword);
        }

        // Save the updated user
        usersRepository.save(user);
    }
}
