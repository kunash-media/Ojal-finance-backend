package com.ojal.controller;

import com.ojal.model_entity.UsersEntity;
import com.ojal.model_entity.dto.request.UserRegistrationDto;
import com.ojal.model_entity.dto.response.UserAccountsResponse;
import com.ojal.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin("*")
@RestController
@RequestMapping("/api/users")
public class UsersController {
    private final UsersService userService;

    @Autowired
    public UsersController(UsersService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationDto> registerUser(@RequestBody UserRegistrationDto request) {
        UsersEntity user = userService.createUser(request);
        UserRegistrationDto response = new UserRegistrationDto(user.getUserId(), user.getName(), user.getEmail(),user.getRole());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("details/{userId}")
    public ResponseEntity<?> getUserAccounts(@PathVariable String userId) {
        try {

            UsersEntity user = userService.findByUserId(userId);

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


}
