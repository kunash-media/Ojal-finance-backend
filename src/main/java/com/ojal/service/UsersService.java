package com.ojal.service;

import com.ojal.global_exception.ResourceNotFoundException;
import com.ojal.global_exception.UnauthorizedException;
import com.ojal.model_entity.UsersEntity;
import com.ojal.model_entity.dto.request.UserRegistrationDto;
import com.ojal.model_entity.dto.request.UserUpdateDto;
import com.ojal.model_entity.dto.response.UserDto;
import org.apache.coyote.BadRequestException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface UsersService {

    // Create a new user with documents
    UsersEntity createUserWithDocuments(
            UserRegistrationDto userData,
            MultipartFile panCard,
            MultipartFile aadharCard,
            MultipartFile passportImg,
            MultipartFile voterIdImg) throws IOException;

    // Get document status for a user
    Map<String, Boolean> getDocumentsStatus(String userId);

    // Get a user by ID
    UserDto getUserById(String userId) throws ResourceNotFoundException;

    // Update a user (PUT - full update)
    UserDto updateUser(String userId, UserUpdateDto userUpdateDto) throws ResourceNotFoundException, BadRequestException;

    // Partially update a user (PATCH)
    UserDto patchUser(String userId, Map<String, Object> updates) throws ResourceNotFoundException, BadRequestException;

    // Delete a user
    void deleteUser(String userId) throws ResourceNotFoundException;

    // Get all users (admin only)
    List<UserDto> getAllUsers(String role) throws UnauthorizedException;
}
