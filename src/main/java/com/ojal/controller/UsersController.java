package com.ojal.controller;

import com.ojal.model_entity.UsersEntity;
import com.ojal.model_entity.dto.request.UserRegistrationDto;
import com.ojal.model_entity.dto.response.GetAllUserDto;
import com.ojal.model_entity.dto.response.UserAccountsResponse;
import com.ojal.model_entity.dto.response.UserRegistrationResponseDto;
import com.ojal.repository.UsersRepository;
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
import java.util.List;
import java.util.Map;


@CrossOrigin("*")
@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final UsersService userService;
    private final UsersRepository usersRepository;

    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);


    @Autowired
    public UsersController(UsersService userService, UsersRepository usersRepository) {
        this.userService = userService;
        this.usersRepository = usersRepository;
    }

    //-------------- User registration ---------------------//
//    @PostMapping("/register")
//    public ResponseEntity<UserRegistrationDto> registerUser(@RequestBody UserRegistrationDto request) {
//        UsersEntity user = userService.createUser(request);
//        UserRegistrationDto response = new UserRegistrationDto(user.getUserId(), user.getName(), user.getEmail(),user.getRole());
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }

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

            // Prepare response
            UserRegistrationResponseDto response = new UserRegistrationResponseDto(
                    user.getUserId(), user.getName(), user.getEmail(), user.getRole()
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


    //--------------- Get User Details -----------------//
    @GetMapping("details/{userId}")
    public ResponseEntity<?> getUserAccounts(@PathVariable String userId) {

        try {
            UsersEntity user = usersRepository.findByUserId(userId);

            if (user == null) {
                return new ResponseEntity<>("User not found with ID: " + userId, HttpStatus.NOT_FOUND);
            }
            UserAccountsResponse response = new UserAccountsResponse(user);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving user accounts: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ------------------- Update User Information ----------------//
    @PatchMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody UserRegistrationDto userRegistrationDto) {
        try {
            userService.updateUser(userId, userRegistrationDto);
            return ResponseEntity.ok().body("User updated successfully");
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating user: " + e.getMessage());
        }
    }

    // -------------------- Get All User List -------------------//
    @GetMapping("/get-all-users")
    public ResponseEntity<List<GetAllUserDto>> getAllUsers(){
        List<GetAllUserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }



}
