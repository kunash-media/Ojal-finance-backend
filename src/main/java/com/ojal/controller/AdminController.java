package com.ojal.controller;

import com.ojal.model_entity.dto.request.AdminDTO;
import com.ojal.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/admins")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // Create new admin
    @PostMapping("/register")
    public ResponseEntity<AdminDTO> createAdmin(@RequestBody AdminDTO adminDTO) {
        AdminDTO createdAdmin = adminService.createAdmin(adminDTO);
        return new ResponseEntity<>(createdAdmin, HttpStatus.CREATED);
    }

    // Get admin by ID
    @GetMapping("/{adminId}")
    public ResponseEntity<AdminDTO> getAdminById(@PathVariable String adminId) {
        AdminDTO adminDTO = adminService.getAdminById(adminId);
        return ResponseEntity.ok(adminDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<AdminDTO> loginAdmin(
            @RequestParam String username,
            @RequestParam String password) {

        AdminDTO adminDTO = adminService.getAdminByCredentials(username, password);
        return ResponseEntity.ok(adminDTO);
    }


    // Get all admins
    @GetMapping
    public ResponseEntity<List<AdminDTO>> getAllAdmins() {
        List<AdminDTO> admins = adminService.getAllAdmins();
        return ResponseEntity.ok(admins);
    }

    // Update admin
    @PutMapping("/{adminId}")
    public ResponseEntity<AdminDTO> updateAdmin(
            @PathVariable String adminId,
            @RequestBody AdminDTO adminDTO) {
        AdminDTO updatedAdmin = adminService.updateAdmin(adminId, adminDTO);
        return ResponseEntity.ok(updatedAdmin);
    }

    @PatchMapping("/{adminId}")
    public ResponseEntity<AdminDTO> partialUpdateAdmin(
            @PathVariable String adminId,
            @RequestBody Map<String, Object> updates) {

        // First, get the existing admin
        AdminDTO existingAdmin = adminService.getAdminById(adminId);

        // Apply only the fields that are present in the updates map
        if (updates.containsKey("fullName")) {
            existingAdmin.setFullName((String) updates.get("fullName"));
        }

        if (updates.containsKey("phone")) {
            existingAdmin.setPhone((String) updates.get("phone"));
        }

        if (updates.containsKey("altPhone")) {
            existingAdmin.setAltPhone((String) updates.get("altPhone"));
        }

        if (updates.containsKey("email")) {
            existingAdmin.setEmail((String) updates.get("email"));
        }

        if (updates.containsKey("gender")) {
            existingAdmin.setGender((String) updates.get("gender"));
        }

        if (updates.containsKey("branchName")) {
            existingAdmin.setBranchName((String) updates.get("branchName"));
        }

        if (updates.containsKey("password")) {
            existingAdmin.setPassword((String) updates.get("password"));
        }

        if (updates.containsKey("role")) {
            existingAdmin.setRole((String) updates.get("role"));
        }

        // Call the update service with the modified DTO
        AdminDTO updatedAdmin = adminService.updateAdmin(adminId, existingAdmin);

        return ResponseEntity.ok(updatedAdmin);
    }

    // Delete admin
    @DeleteMapping("/{adminId}")
    public ResponseEntity<Map<String, String>> deleteAdmin(@PathVariable String adminId) {
        adminService.deleteAdmin(adminId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Admin with ID: " + adminId + " deleted successfully");
        return ResponseEntity.ok(response);
    }

    // Check if email exists
    @GetMapping("/email-exists")
    public ResponseEntity<Map<String, Boolean>> checkEmailExists(@RequestParam String email) {
        boolean exists = adminService.isEmailExists(email);

        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    // Get admin by email
    @GetMapping("/email/{email}")
    public ResponseEntity<AdminDTO> getAdminByEmail(@PathVariable String email) {
        AdminDTO adminDTO = adminService.getAdminByEmail(email);
        return ResponseEntity.ok(adminDTO);
    }

    // Get admin by phone
    @GetMapping("/phone/{phone}")
    public ResponseEntity<AdminDTO> getAdminByPhone(@PathVariable String phone) {
        AdminDTO adminDTO = adminService.getAdminByPhone(phone);
        return ResponseEntity.ok(adminDTO);
    }

    @GetMapping("/get-branch-list")
    public ResponseEntity<List<String>> getAllBranchNames() {
        List<String> branchNames = adminService.getAllBranchNames();
        return ResponseEntity.ok(branchNames);
    }
}