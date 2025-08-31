package com.ojal.service;

import com.ojal.model_entity.LoanAccountsEntity;
import com.ojal.model_entity.dto.request.LoanAccountsDto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface LoanAccountsService {
    LoanAccountsEntity createAccount(LoanAccountsDto request, MultipartFile photo, MultipartFile applicantSignature, MultipartFile guarantorSignature, MultipartFile branchSeal) throws IOException;
    LoanAccountsEntity findByAccountNumber(String accountNumber);
    List<LoanAccountsEntity> findAll();
    LoanAccountsEntity updateAccount(String accountNumber, LoanAccountsDto request, MultipartFile photo, MultipartFile applicantSignature, MultipartFile guarantorSignature, MultipartFile branchSeal) throws IOException;
    LoanAccountsEntity patchAccount(String accountNumber, LoanAccountsDto request, MultipartFile photo, MultipartFile applicantSignature, MultipartFile guarantorSignature, MultipartFile branchSeal) throws IOException;
    String delete(String accountNumber);

    Optional<LoanAccountsEntity> findByApplicationNo(String applicationNo);

}
