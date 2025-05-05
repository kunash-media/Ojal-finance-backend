package com.ojal.service;

import com.ojal.model_entity.UsersEntity;
import com.ojal.model_entity.dto.request.UserRegistrationDto;
import com.ojal.model_entity.dto.response.GetAllUserDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface UsersService {


    UsersEntity createUserWithDocuments(
            UserRegistrationDto userData,
            MultipartFile panCard,
            MultipartFile aadharCard,
            MultipartFile passportImg,
            MultipartFile voterIdImg) throws IOException;

    Map<String, Boolean> getDocumentsStatus(String userId);


//    UsersEntity createUser(UserRegistrationDto request);

    void updateUser(String userId, UserRegistrationDto userRegistrationDto);

    List<GetAllUserDto> getAllUsers();
}
