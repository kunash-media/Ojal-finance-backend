package com.ojal.service;

import com.ojal.model_entity.dto.request.AdminDTO;

import java.util.List;

public interface AdminService {

    // Create
    AdminDTO createAdmin(AdminDTO adminDTO);

    AdminDTO getAdminByCredentials(String username, String password);

    // Read
    AdminDTO getAdminById(String adminId);

    List<AdminDTO> getAllAdmins();

    AdminDTO getAdminByEmail(String email);

    // Update
    AdminDTO updateAdmin(String adminId, AdminDTO adminDTO);

    // Delete
    void deleteAdmin(String adminId);

    // Additional functionality
    boolean isEmailExists(String email);

    AdminDTO getAdminByPhone(String phone);
}