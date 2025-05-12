package com.ojal.service.service_impl;

import com.ojal.bcrypt.BcryptEncoderConfig;
import com.ojal.global_exception.ResourceNotFoundException;
import com.ojal.model_entity.UsersEntity;
import com.ojal.model_entity.dto.request.UserRegistrationDto;
import com.ojal.model_entity.dto.response.GetAllUserDto;
import com.ojal.repository.UsersRepository;
import com.ojal.service.UsersService;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final BcryptEncoderConfig passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UsersServiceImpl.class);


    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository,
                            BcryptEncoderConfig passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder =   passwordEncoder;
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

        // Create new user
        UsersEntity user = new UsersEntity();
        user.setName(userData.getName());
        user.setEmail(userData.getEmail());
        user.setPassword(passwordEncoder.encode(userData.getPassword())); // Ensure you encode the password

        // Set creation time with format of 12hr-am/pm
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");

        String formattedTime = now.format(formatter);
        user.setCreatedAt(formattedTime);

        user.setRole(userData.getRole() != null ? userData.getRole() : "ROLE_USER"); // Default role if not specified

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


    // Adding the getDocumentsStatus method
    @Override
    public Map<String, Boolean> getDocumentsStatus(String userId) {

        UsersEntity user = usersRepository.findByUserId(userId);

        if(user == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found with ID: "+userId);
        }

        Map<String, Boolean> documentStatus = new HashMap<>();
        documentStatus.put("panCard", user.getPanCard() != null);
        documentStatus.put("aadharCard", user.getAadharCard() != null);
        documentStatus.put("passportImg", user.getPassPortImg() != null);
        documentStatus.put("voterIdImg", user.getVoterIdImg() != null);

        return documentStatus;
    }

//    @Override
//    @Transactional
//    public UsersEntity createUser(UserRegistrationDto request) {
//        // Check if email already exists
//        if (usersRepository.existsByEmail(request.getEmail())) {
//            throw new IllegalArgumentException("Email already registered: " + request.getEmail());
//        }
//
//        UsersEntity user = new UsersEntity();
//        user.setName(request.getName());
//        user.setEmail(request.getEmail());
//
//        String encodedPassword = passwordEncoder.encode(request.getPassword());
//        user.setPassword(encodedPassword);
//        user.setRole("ROLE_USER");
//        user.setUserRepository(usersRepository);
//
//        return usersRepository.save(user);
//    }

    @Override
    @Transactional(readOnly = true)
    public List<GetAllUserDto> getAllUsers() {
        return usersRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    //----- Helper method for convert dto ------
    private GetAllUserDto convertToDto(UsersEntity user) {
        return new GetAllUserDto(
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }


    @Override
    @Transactional
    public void updateUser(String userId, UserRegistrationDto userRegistrationDto) {

        UsersEntity user = usersRepository.findByUserId(userId);

        if(user == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found with ID: "+userId);
        }

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
