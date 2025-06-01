package com.ojal.controller;

import com.ojal.global_exception.ResourceNotFoundException;
import com.ojal.global_exception.UnauthorizedException;
import com.ojal.model_entity.UsersEntity;
import com.ojal.model_entity.dto.request.UserRegistrationDto;
import com.ojal.model_entity.dto.request.UserUpdateDto;
import com.ojal.model_entity.dto.response.UserDto;
import com.ojal.model_entity.dto.response.UserRegistrationResponseDto;
import com.ojal.service.UsersService;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final UsersService userService;
    private final Logger logger = LoggerFactory.getLogger(UsersController.class);

    @Autowired
    public UsersController(UsersService userService) {
        this.userService = userService;
    }

    //-------------------- User registration ---------------------//
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserRegistrationResponseDto> registerUserWithDocuments(
            @RequestPart("userData") UserRegistrationDto userData,
            @RequestPart(value = "panCard", required = false) MultipartFile panCard,
            @RequestPart(value = "aadharCard", required = false) MultipartFile aadharCard,
            @RequestPart(value = "passPortImg", required = false) MultipartFile passPortImg,
            @RequestPart(value = "voterIdImg", required = false) MultipartFile voterIdImg) throws BadRequestException {

        logger.info("Registering new user with email: {} and documents", userData.getEmail());

        try {
            // Create user with documents
            UsersEntity user = userService.createUserWithDocuments(
                    userData, panCard, aadharCard, passPortImg, voterIdImg
            );

            // Prepare response with updated fields
            UserRegistrationResponseDto response = new UserRegistrationResponseDto(
                    user.getUserId(),
                    user.getFirstName(),
                    user.getMiddleName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getMobile(),
                    user.getAltMobile(),
                    user.getGender(),
                    user.getDob(),
                    user.getAddress(),
                    user.getPincode(),
                    user.getBranch(),
                    user.getRole()
            );

            // Add document status to response
            Map<String, Boolean> documentStatus = userService.getDocumentsStatus(user.getUserId());
            response.setDocumentStatus(documentStatus);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IOException e) {
            logger.error("Error processing document files: {}", e.getMessage());
            throw new BadRequestException("Error processing document files: " + e.getMessage());
        }
    }

    //-------------------- Get user by ID ---------------------//
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String userId) {
        logger.info("Fetching user with ID: {}", userId);

        try {
            UserDto user = userService.getUserById(userId);
            return ResponseEntity.ok(user);
        } catch (ResourceNotFoundException e) {
            logger.error("User not found: {}", e.getMessage());
            throw e;
        }
    }

    //-------------------- Update user (PUT) ---------------------//
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable String userId,
            @RequestBody UserUpdateDto userUpdateDto) {

        logger.info("Updating user with ID: {}", userId);

        try {
            UserDto updatedUser = userService.updateUser(userId, userUpdateDto);
            return ResponseEntity.ok(updatedUser);
        } catch (ResourceNotFoundException e) {
            logger.error("User not found: {}", e.getMessage());
            throw e;
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        }
    }

    //-------------------- Partially update user (PATCH) ---------------------//
    @PatchMapping("/{userId}")
    public ResponseEntity<UserDto> patchUser(
            @PathVariable String userId,
            @RequestBody Map<String, Object> updates) throws BadRequestException {

        logger.info("Partially updating user with ID: {}", userId);

        try {
            UserDto patchedUser = userService.patchUser(userId, updates);
            return ResponseEntity.ok(patchedUser);
        } catch (ResourceNotFoundException | BadRequestException e) {
            logger.error("User not found: {}", e.getMessage());
            throw e;
        }
    }

    //-------------------- Delete user ---------------------//
    @DeleteMapping("/{userId}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable String userId) {
        logger.info("Deleting user with ID: {}", userId);

        try {
            userService.deleteUser(userId);

            Map<String, String> response = new HashMap<>();
            response.put("message", "User deleted successfully");
            response.put("userId", userId);

            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            logger.error("User not found: {}", e.getMessage());
            throw e;
        }
    }

    //-------------------- Get all users (admin only) ---------------------//
    @GetMapping("/get-all-users")
    public ResponseEntity<?> getAllUsers(@RequestParam(required = false) String role) {
        logger.info("Fetching all users with role: {}", role);

        try {
            // Check if role is provided
            if (role == null || role.isEmpty()) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Role parameter is required");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // Get all users (will throw UnauthorizedException if not admin)
            List<UserDto> users = userService.getAllUsers(role);
            return ResponseEntity.ok(users);
        } catch (UnauthorizedException e) {
            logger.error("Unauthorized access: {}", e.getMessage());

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }
    }

    //--------- get all users by branch --------------//
    @GetMapping("/get-all-branch")
    public ResponseEntity<List<UserDto>> allUsersByBranch(@RequestParam("branchName") String branch){
        return ResponseEntity.ok(userService.getUsersByBranch(branch));
    }




}