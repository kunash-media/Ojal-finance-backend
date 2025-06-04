package com.ojal.service.service_impl;

import com.ojal.bcrypt.BcryptEncoderConfig;
import com.ojal.model_entity.AdminEntity;
import com.ojal.model_entity.dto.request.AdminDTO;
import com.ojal.repository.AdminRepository;
import com.ojal.service.AdminService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final BcryptEncoderConfig passwordEncoder;

    @Autowired
    public AdminServiceImpl(AdminRepository adminRepository, BcryptEncoderConfig passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AdminDTO createAdmin(AdminDTO adminDTO) {

        Optional<AdminEntity> existUserName = adminRepository.findByUsername(adminDTO.getUsername());

        if (adminDTO.getUsername() == null) {
            throw new IllegalArgumentException("Username is required");
        }
        if (existUserName.isPresent()) {
            throw new IllegalArgumentException("Username is Already Exists, Choose Different one");
        }
        // Check if email already exists
        if (adminRepository.existsByEmail(adminDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        // Convert DTO to Entity
        AdminEntity adminEntity = convertToEntity(adminDTO);

        // Encode password
        adminEntity.setPassword(passwordEncoder.encode(adminDTO.getPassword()));

        AdminEntity savedAdmin = adminRepository.save(adminEntity);

        // Convert saved entity back to DTO and return
        return convertToDTO(savedAdmin);
    }

    @Override
    public AdminDTO getAdminById(String adminId) {
        AdminEntity adminEntity = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + adminId));

        return convertToDTO(adminEntity);
    }

    @Override
    public List<AdminDTO> getAllAdmins() {
        List<AdminEntity> adminEntities = adminRepository.findAll();

        return adminEntities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AdminDTO getAdminByEmail(String email) {
        AdminEntity adminEntity = adminRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found with email: " + email));

        return convertToDTO(adminEntity);
    }

    @Override
    public AdminDTO updateAdmin(String adminId, AdminDTO adminDTO) {
        AdminEntity existingAdmin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + adminId));

        // Check if email changed and if it's already in use by another admin
        if (!existingAdmin.getEmail().equals(adminDTO.getEmail()) &&
                adminRepository.existsByEmail(adminDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Update fields
        existingAdmin.setFullName(adminDTO.getFullName());
        existingAdmin.setPhone(adminDTO.getPhone());
        existingAdmin.setAltPhone(adminDTO.getAltPhone());
        existingAdmin.setEmail(adminDTO.getEmail());
        existingAdmin.setGender(adminDTO.getGender());
        existingAdmin.setBranchName(adminDTO.getBranchName());
        existingAdmin.setRole(adminDTO.getRole());

        // Update password if provided
        if (adminDTO.getPassword() != null && !adminDTO.getPassword().isEmpty()) {
            existingAdmin.setPassword(passwordEncoder.encode(adminDTO.getPassword()));
        }

        // Save updated entity
        AdminEntity updatedAdmin = adminRepository.save(existingAdmin);

        return convertToDTO(updatedAdmin);
    }

    @Override
    public void deleteAdmin(String adminId) {
        if (!adminRepository.existsById(adminId)) {
            throw new RuntimeException("Admin not found with id: " + adminId);
        }

        adminRepository.deleteById(adminId);
    }

    @Override
    public boolean isEmailExists(String email) {
        return adminRepository.existsByEmail(email);
    }

    @Override
    public AdminDTO getAdminByPhone(String phone) {
        AdminEntity adminEntity = adminRepository.findByPhoneOrAltPhone(phone, phone)
                .orElseThrow(() -> new RuntimeException("Admin not found with phone: " + phone));

        return convertToDTO(adminEntity);
    }

    // Helper methods for entity<->DTO conversion
    private AdminDTO convertToDTO(AdminEntity adminEntity) {
        AdminDTO adminDTO = new AdminDTO();
        BeanUtils.copyProperties(adminEntity, adminDTO);
        adminDTO.setPassword(null); // Don't expose password in DTO
        return adminDTO;
    }

    private AdminEntity convertToEntity(AdminDTO adminDTO) {
        AdminEntity adminEntity = new AdminEntity();

        // Explicitly set all required fields to ensure they're properly mapped
        adminEntity.setFullName(adminDTO.getFullName());
        adminEntity.setEmail(adminDTO.getEmail());
        adminEntity.setPhone(adminDTO.getPhone());
        adminEntity.setAltPhone(adminDTO.getAltPhone());
        adminEntity.setGender(adminDTO.getGender());
        adminEntity.setBranchName(adminDTO.getBranchName());
        adminEntity.setUsername(adminDTO.getUsername());

        adminEntity.setRole("ROLE_ADMIN");

        // Handle adminId separately - don't overwrite if it exists
        if (adminDTO.getAdminId() != null) {
            adminEntity.setAdminId(adminDTO.getAdminId());
        }

        // Handle password separately - only set if provided
        if (adminDTO.getPassword() != null && !adminDTO.getPassword().isEmpty()) {
            adminEntity.setPassword(adminDTO.getPassword());
        }

        return adminEntity;
    }

    @Override
    public AdminDTO getAdminByCredentials(String username, String password) {
        // Find admin by email (username)
        AdminEntity adminEntity = adminRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        // Check if provided password matches the encoded password
        if (!passwordEncoder.matches(password, adminEntity.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // If we get here, credentials are valid
        return convertToDTO(adminEntity);
    }


    @Override
    public List<String> getAllBranchNames() {
        return adminRepository.findAllDistinctBranchNames();
    }
}