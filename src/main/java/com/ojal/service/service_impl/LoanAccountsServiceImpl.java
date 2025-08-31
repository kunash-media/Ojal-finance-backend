package com.ojal.service.service_impl;

import com.ojal.model_entity.LoanAccountsEntity;
import com.ojal.model_entity.dto.request.LoanAccountsDto;
import com.ojal.repository.LoanAccountsRepository;

import com.ojal.service.LoanAccountsService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class LoanAccountsServiceImpl implements LoanAccountsService {

    private final LoanAccountsRepository loanAccountsRepository;

    @Autowired
    public LoanAccountsServiceImpl(LoanAccountsRepository loanAccountsRepository) {
        this.loanAccountsRepository = loanAccountsRepository;
    }

    @Override
    @Transactional
    public LoanAccountsEntity createAccount(LoanAccountsDto request, MultipartFile photo, MultipartFile applicantSignature, MultipartFile guarantorSignature, MultipartFile branchSeal) throws IOException {
        LoanAccountsEntity loanAccount = new LoanAccountsEntity();

        // Set account number from request if provided, else generate
        String accountNumber = loanAccount.generateAccountNumber();
        loanAccount.setAccountNumber(accountNumber);

        setFieldsFromDto(loanAccount, request);

        // Set images if provided
        if (photo != null && !photo.isEmpty()) {
            loanAccount.setPhoto(photo.getBytes());
        }
        if (applicantSignature != null && !applicantSignature.isEmpty()) {
            loanAccount.setApplicantSignature(applicantSignature.getBytes());
        }
        if (guarantorSignature != null && !guarantorSignature.isEmpty()) {
            loanAccount.setGuarantorSignature(guarantorSignature.getBytes());
        }
        if (branchSeal != null && !branchSeal.isEmpty()) {
            loanAccount.setBranchSeal(branchSeal.getBytes());
        }

        return loanAccountsRepository.save(loanAccount);
    }


    //-------------- find loan account by account number-----------//
    @Override
    public LoanAccountsEntity findByAccountNumber(String accountNumber) {
        return loanAccountsRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new EntityNotFoundException("Loan account not found with number: " + accountNumber));
    }

    //----------- find all-loans entry ---------------------//
    @Override
    public List<LoanAccountsEntity> findAll() {
        return loanAccountsRepository.findAll();
    }

    //---------- find loan application by application number ----------------//
    @Override
    public Optional<LoanAccountsEntity> findByApplicationNo(String applicationNo) {
        return loanAccountsRepository.findByApplicationNo(applicationNo);
    }

    //---------------------update loan account by acc number --------------//
    @Override
    @Transactional
    public LoanAccountsEntity updateAccount(String accountNumber, LoanAccountsDto request, MultipartFile photo, MultipartFile applicantSignature, MultipartFile guarantorSignature, MultipartFile branchSeal) throws IOException {
        LoanAccountsEntity account = findByAccountNumber(accountNumber);

        // Full update: overwrite all fields
        setFieldsFromDto(account, request);

        // Update images if provided
        if (photo != null && !photo.isEmpty()) {
            account.setPhoto(photo.getBytes());
        }
        if (applicantSignature != null && !applicantSignature.isEmpty()) {
            account.setApplicantSignature(applicantSignature.getBytes());
        }
        if (guarantorSignature != null && !guarantorSignature.isEmpty()) {
            account.setGuarantorSignature(guarantorSignature.getBytes());
        }
        if (branchSeal != null && !branchSeal.isEmpty()) {
            account.setBranchSeal(branchSeal.getBytes());
        }

        return loanAccountsRepository.save(account);
    }

    //----------------------- patch changes by acc number -----------------//
    @Override
    @Transactional
    public LoanAccountsEntity patchAccount(String accountNumber, LoanAccountsDto request, MultipartFile photo, MultipartFile applicantSignature, MultipartFile guarantorSignature, MultipartFile branchSeal) throws IOException {
        LoanAccountsEntity account = findByAccountNumber(accountNumber);

        // Partial update: only set non-null fields
        if (request.getPurposeOfLoan() != null) account.setPurposeOfLoan(request.getPurposeOfLoan());
        if (request.getLoanScheme() != null) account.setLoanScheme(request.getLoanScheme());
        if (request.getLoanAmount() != null) account.setLoanAmount(request.getLoanAmount());
        if (request.getRoi() != null) account.setRoi(request.getRoi());
        if (request.getAppliedDate() != null) account.setAppliedDate(request.getAppliedDate());
        if (request.getTenure() != null) account.setTenure(request.getTenure());
        if (request.getEmiAmount() != null) account.setEmiAmount(request.getEmiAmount());
        if (request.getMemberName() != null) account.setMemberName(request.getMemberName());
        if (request.getFatherName() != null) account.setFatherName(request.getFatherName());
        if (request.getMobile() != null) account.setMobile(request.getMobile());
        if (request.getDateOfJoining() != null) account.setDateOfJoining(request.getDateOfJoining());
        if (request.getMemberType() != null) account.setMemberType(request.getMemberType());
        if (request.getBranchName() != null) account.setBranchName(request.getBranchName());
        if (request.getAddress() != null) account.setAddress(request.getAddress());
        if (request.getPanNumber() != null) account.setPanNumber(request.getPanNumber());
        if (request.getAdhaarNumber() != null) account.setAdhaarNumber(request.getAdhaarNumber());
        if (request.getGrantorName() != null) account.setGrantorName(request.getGrantorName());
        if (request.getGrantorAddress() != null) account.setGrantorAddress(request.getGrantorAddress());
        if (request.getGrantorMobile() != null) account.setGrantorMobile(request.getGrantorMobile());
        if (request.getNomineeName() != null) account.setNomineeName(request.getNomineeName());
        if (request.getNomineeAddress() != null) account.setNomineeAddress(request.getNomineeAddress());
        if (request.getNomineeMobile() != null) account.setNomineeMobile(request.getNomineeMobile());
        if (request.getProcessingFee() != null) account.setProcessingFee(request.getProcessingFee());
        if (request.getDisbursedAmount() != null) account.setDisbursedAmount(request.getDisbursedAmount());

        // Update images if provided
        if (photo != null && !photo.isEmpty()) {
            account.setPhoto(photo.getBytes());
        }
        if (applicantSignature != null && !applicantSignature.isEmpty()) {
            account.setApplicantSignature(applicantSignature.getBytes());
        }
        if (guarantorSignature != null && !guarantorSignature.isEmpty()) {
            account.setGuarantorSignature(guarantorSignature.getBytes());
        }
        if (branchSeal != null && !branchSeal.isEmpty()) {
            account.setBranchSeal(branchSeal.getBytes());
        }

        return loanAccountsRepository.save(account);
    }

    //-------------------delete loan account by acc number -------------------//
    @Override
    @Transactional
    public String delete(String accountNumber) {
        LoanAccountsEntity account = findByAccountNumber(accountNumber);
        loanAccountsRepository.delete(account);
        return "Loan Account " + accountNumber + " deleted successfully!!";
    }

    //------------------- set entity fields from dto -------------------//
    private void setFieldsFromDto(LoanAccountsEntity entity, LoanAccountsDto dto) {
        entity.setApplicationNo(dto.getApplicationNo());
        entity.setPurposeOfLoan(dto.getPurposeOfLoan());
        entity.setLoanScheme(dto.getLoanScheme());
        entity.setLoanAmount(dto.getLoanAmount());
        entity.setRoi(dto.getRoi());
        entity.setAppliedDate(dto.getAppliedDate());
        entity.setTenure(dto.getTenure());
        entity.setEmiAmount(dto.getEmiAmount());
        entity.setMemberName(dto.getMemberName());
        entity.setFatherName(dto.getFatherName());
        entity.setMobile(dto.getMobile());
        entity.setDateOfJoining(dto.getDateOfJoining());
        entity.setMemberType(dto.getMemberType());
        entity.setBranchName(dto.getBranchName());
        entity.setAddress(dto.getAddress());
        entity.setPanNumber(dto.getPanNumber());
        entity.setAdhaarNumber(dto.getAdhaarNumber());
        entity.setGrantorName(dto.getGrantorName());
        entity.setGrantorAddress(dto.getGrantorAddress());
        entity.setGrantorMobile(dto.getGrantorMobile());
        entity.setNomineeName(dto.getNomineeName());
        entity.setNomineeAddress(dto.getNomineeAddress());
        entity.setNomineeMobile(dto.getNomineeMobile());
        entity.setProcessingFee(dto.getProcessingFee());
        entity.setDisbursedAmount(dto.getDisbursedAmount());
    }
}