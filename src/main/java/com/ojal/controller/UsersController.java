package com.ojal.controller;

import com.ojal.model_entity.UsersEntity;
import com.ojal.model_entity.dto.request.UserRegistrationDto;
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
    public ResponseEntity<UserResponse> registerUser(@RequestBody UserRegistrationDto request) {
        UsersEntity user = userService.createUser(request);
        UserResponse response = new UserResponse(user.getUserId(), user.getName(), user.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String userId) {
        UsersEntity user = userService.findByUserId(userId);
        UserResponse response = new UserResponse(user.getUserId(), user.getName(), user.getEmail());
        return ResponseEntity.ok(response);
    }

    // Response DTO
    public static class UserResponse {
        private String userId;
        private String name;
        private String email;

        public UserResponse(String userId, String name, String email) {
            this.userId = userId;
            this.name = name;
            this.email = email;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
