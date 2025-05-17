package com.ojal.service.service_impl;

import com.ojal.bcrypt.BcryptEncoderConfig;
import com.ojal.global_exception.ResourceNotFoundException;
import com.ojal.global_exception.UnauthorizedException;
import com.ojal.model_entity.UsersEntity;
import com.ojal.model_entity.dto.request.UserRegistrationDto;
import com.ojal.model_entity.dto.request.UserUpdateDto;
import com.ojal.model_entity.dto.response.UserDto;
import com.ojal.repository.UsersRepository;
import com.ojal.service.UsersService;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final BcryptEncoderConfig passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(UsersServiceImpl.class);

    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository, BcryptEncoderConfig passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UsersEntity createUserWithDocuments(
            UserRegistrationDto userData,
            MultipartFile panCard,
            MultipartFile aadharCard,
            MultipartFile passPortImg,
            MultipartFile voterIdImg) throws IOException {

        // Check if email already exists
        if (usersRepository.existsByEmail(userData.getEmail())) {
            logger.error("Email already registered: {}", userData.getEmail());
            throw new BadRequestException("Email already registered: " + userData.getEmail());
        }

        // Check if mobile already exists
        if (usersRepository.existsByMobile(userData.getMobile())) {
            logger.error("Mobile number already registered: {}", userData.getMobile());
            throw new BadRequestException("Mobile number already registered: " + userData.getMobile());
        }

        // Create new user with updated fields
        UsersEntity user = new UsersEntity();

        // Set all the new fields
        user.setFirstName(userData.getFirstName());
        user.setMiddleName(userData.getMiddleName());
        user.setLastName(userData.getLastName());
        user.setEmail(userData.getEmail());
        user.setMobile(userData.getMobile());
        user.setAltMobile(userData.getAltMobile());
        user.setGender(userData.getGender());
        user.setDob(userData.getDob());
        user.setAddress(userData.getAddress());
        user.setPincode(userData.getPincode());
        user.setBranch(userData.getBranch());


        // Set creation time with format of 12hr-am/pm
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
        String formattedTime = now.format(formatter);
        user.setCreatedAt(formattedTime);

        // Set role (default to ROLE_USER if not specified)
        user.setRole(userData.getRole() != null ? userData.getRole() : "ROLE_USER");

        // Set user repository for ID generation
        user.setUserRepository(usersRepository);

        // Process documents if provided
        if (panCard != null && !panCard.isEmpty()) {
            user.setPanCard(panCard.getBytes());
        }

        if (aadharCard != null && !aadharCard.isEmpty()) {
            user.setAadharCard(aadharCard.getBytes());
        }

        if (passPortImg != null && !passPortImg.isEmpty()) {
            user.setPassPortImg(passPortImg.getBytes());
        }

        if (voterIdImg != null && !voterIdImg.isEmpty()) {
            user.setVoterIdImg(voterIdImg.getBytes());
        }

        // Save user to database
        return usersRepository.save(user);
    }

    // Helper method for getting document status
    @Override
    public Map<String, Boolean> getDocumentsStatus(String userId) {
        UsersEntity user = usersRepository.findByUserId(userId);

        if(user == null){
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }

        Map<String, Boolean> documentStatus = new HashMap<>();
        documentStatus.put("panCard", user.getPanCard() != null);
        documentStatus.put("aadharCard", user.getAadharCard() != null);
        documentStatus.put("passportImg", user.getPassPortImg() != null);
        documentStatus.put("voterIdImg", user.getVoterIdImg() != null);

        return documentStatus;
    }

    @Override
    public UserDto getUserById(String userId) throws ResourceNotFoundException {
        UsersEntity user = usersRepository.findByUserId(userId);

        if (user == null) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }

        // Convert entity to DTO
        UserDto userDto = new UserDto(user);

        // Add document status
        userDto.setDocumentStatus(getDocumentsStatus(userId));

        return userDto;
    }

    @Override
    @Transactional
    public UserDto updateUser(String userId, UserUpdateDto userUpdateDto) throws ResourceNotFoundException, BadRequestException {
        UsersEntity user = usersRepository.findByUserId(userId);

        if (user == null) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }

        // Check if email is being changed and already exists
        if (userUpdateDto.getEmail() != null && !userUpdateDto.getEmail().equals(user.getEmail()) &&
                usersRepository.existsByEmail(userUpdateDto.getEmail())) {
            throw new BadRequestException("Email already registered: " + userUpdateDto.getEmail());
        }

        // Check if mobile is being changed and already exists
        if (userUpdateDto.getMobile() != null && !userUpdateDto.getMobile().equals(user.getMobile()) &&
                usersRepository.existsByMobile(userUpdateDto.getMobile())) {
            throw new BadRequestException("Mobile number already registered: " + userUpdateDto.getMobile());
        }

        // Update all fields
        user.setFirstName(userUpdateDto.getFirstName());
        user.setMiddleName(userUpdateDto.getMiddleName());
        user.setLastName(userUpdateDto.getLastName());
        user.setEmail(userUpdateDto.getEmail());
        user.setMobile(userUpdateDto.getMobile());
        user.setAltMobile(userUpdateDto.getAltMobile());
        user.setGender(userUpdateDto.getGender());
        user.setDob(userUpdateDto.getDob());
        user.setAddress(userUpdateDto.getAddress());
        user.setPincode(userUpdateDto.getPincode());
        user.setBranch(userUpdateDto.getBranch());
        user.setRole(userUpdateDto.getRole());

        // Save updated user
        UsersEntity updatedUser = usersRepository.save(user);

        // Convert to DTO and return
        UserDto userDto = new UserDto(updatedUser);
        userDto.setDocumentStatus(getDocumentsStatus(userId));

        return userDto;
    }

    @Override
    @Transactional
    public UserDto patchUser(String userId, Map<String, Object> updates) throws ResourceNotFoundException, BadRequestException {
        UsersEntity user = usersRepository.findByUserId(userId);

        if (user == null) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }

        // Check if email is being updated and already exists
        if (updates.containsKey("email")) {
            String newEmail = (String) updates.get("email");
            if (!newEmail.equals(user.getEmail()) && usersRepository.existsByEmail(newEmail)) {
                throw new BadRequestException("Email already registered: " + newEmail);
            }
        }

        // Check if mobile is being updated and already exists
        if (updates.containsKey("mobile")) {
            String newMobile = (String) updates.get("mobile");
            if (!newMobile.equals(user.getMobile()) && usersRepository.existsByMobile(newMobile)) {
                throw new BadRequestException("Mobile number already registered: " + newMobile);
            }
        }

        // Apply updates to specific fields
        updates.forEach((key, value) -> {
            switch (key) {
                case "firstName":
                    user.setFirstName((String) value);
                    break;
                case "middleName":
                    user.setMiddleName((String) value);
                    break;
                case "lastName":
                    user.setLastName((String) value);
                    break;
                case "email":
                    user.setEmail((String) value);
                    break;
                case "mobile":
                    user.setMobile((String) value);
                    break;
                case "altMobile":
                    user.setAltMobile((String) value);
                    break;
                case "gender":
                    user.setGender((String) value);
                    break;
                case "dob":
                    user.setDob((String) value);
                    break;
                case "address":
                    user.setAddress((String) value);
                    break;
                case "pincode":
                    user.setPincode((String) value);
                    break;
                case "branch":
                    user.setBranch((String) value);
                    break;
                case "role":
                    user.setRole((String) value);
                    break;
                // Don't allow password updates through this method
                // Documents are also not updated through this method
            }
        });

        // Save updated user
        UsersEntity updatedUser = usersRepository.save(user);

        // Convert to DTO and return
        UserDto userDto = new UserDto(updatedUser);
        userDto.setDocumentStatus(getDocumentsStatus(userId));

        return userDto;
    }

    @Override
    @Transactional
    public void deleteUser(String userId) throws ResourceNotFoundException {
        UsersEntity user = usersRepository.findByUserId(userId);

        if (user == null) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }

        // Delete the user
        usersRepository.deleteByUserId(userId);
        logger.info("User deleted with ID: {}", userId);
    }

    @Override
    public List<UserDto> getAllUsers(String role) throws UnauthorizedException {
        // Verify admin role
        if (!"ROLE_ADMIN".equals(role)) {
            throw new UnauthorizedException("Only administrators can access this resource");
        }

        // Get all users
        List<UsersEntity> users = usersRepository.findAll();

        // Convert to DTOs
        List<UserDto> userDtos = new ArrayList<>();
        for (UsersEntity user : users) {
            UserDto dto = new UserDto(user);
            dto.setDocumentStatus(getDocumentsStatus(user.getUserId()));
            userDtos.add(dto);
        }

        return userDtos;
    }
}