package com.ojal.controller;

import com.ojal.model_entity.LoanAccountsEntity;
import com.ojal.model_entity.dto.request.LoanAccountsDto;
import com.ojal.model_entity.dto.response.LoanAccountsResponseDto;
import com.ojal.service.LoanAccountsService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/loans")
public class LoanAccountsController {

    private final LoanAccountsService loanAccountsService;

    private static final Logger logger = LoggerFactory.getLogger(LoanAccountsController.class);


    @Autowired
    public LoanAccountsController(LoanAccountsService loanAccountsService) {
        this.loanAccountsService = loanAccountsService;
    }

    @PostMapping("/create-loan")
    public ResponseEntity<LoanAccountsEntity> createLoanAccount(
            @RequestPart("formData") @Valid LoanAccountsDto request,
            @RequestPart(value = "photo", required = false) MultipartFile photo,
            @RequestPart(value = "applicantSignature", required = false) MultipartFile applicantSignature,
            @RequestPart(value = "guarantorSignature", required = false) MultipartFile guarantorSignature,
            @RequestPart(value = "branchSeal", required = false) MultipartFile branchSeal
    ) throws IOException {
        logger.info("Creating loan account - Application No: {}, Loan Amount: {}",
                request.getApplicationNo(), request.getLoanAmount());
        logger.debug("Received files - Photo: {}, Applicant Signature: {}, Guarantor Signature: {}, Branch Seal: {}",
                photo != null, applicantSignature != null, guarantorSignature != null, branchSeal != null);

        try {
            LoanAccountsEntity account = loanAccountsService.createAccount(request, photo, applicantSignature, guarantorSignature, branchSeal);
            logger.info("Successfully created loan account - Account Number: {}, Application No: {}",
                    account.getAccountNumber(), account.getApplicationNo());
            return ResponseEntity.status(HttpStatus.CREATED).body(account);
        } catch (Exception e) {
            logger.error("Failed to create loan account for Application No: {} - Error: {}",
                    request.getApplicationNo(), e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/get-all-loans")
    public ResponseEntity<List<LoanAccountsResponseDto>> getAllLoans(HttpServletRequest request) {
        logger.info("Fetching all loans - Request from: {}", request.getRemoteAddr());

        try {
            List<LoanAccountsEntity> loans = loanAccountsService.findAll();
            logger.debug("Retrieved {} loan records from database", loans.size());

            // Generate base URL for images
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/api/loans";
            logger.debug("Generated base URL for images: {}", baseUrl);

            // Convert entities to DTOs with image URLs
            List<LoanAccountsResponseDto> response = loans.stream()
                    .map(loan -> new LoanAccountsResponseDto(loan, baseUrl))
                    .collect(Collectors.toList());

            logger.info("Successfully retrieved and processed {} loan accounts", response.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to retrieve all loans - Error: {}", e.getMessage(), e);
            throw e;
        }
    }

    // Updated image endpoint with comprehensive error handling
    @GetMapping("/image/{imageName}")
    public ResponseEntity<byte[]> getImage(@PathVariable String imageName) {
        logger.info("Requesting image: {}", imageName);

        try {
            // Parse image name to get application number and image type
            String[] parts = imageName.split("_");
            logger.debug("Image name parts: {}", Arrays.toString(parts));

            if (parts.length < 2) {
                logger.warn("Invalid image name format received: {}", imageName);
                return ResponseEntity.badRequest().build();
            }

            String applicationNo = parts[0];
            String imageType = String.join("_", Arrays.copyOfRange(parts, 1, parts.length));
            logger.debug("Parsed - Application No: {}, Image Type: {}", applicationNo, imageType);

            // Find loan by application number
            Optional<LoanAccountsEntity> loanOpt = loanAccountsService.findByApplicationNo(applicationNo);
            if (!loanOpt.isPresent()) {
                logger.warn("Loan not found for application number: {}", applicationNo);
                return ResponseEntity.notFound().build();
            }

            LoanAccountsEntity loan = loanOpt.get();
            byte[] imageData = null;
            String contentType = "image/jpeg";

            // Get the appropriate image based on type
            switch (imageType) {
                case "photo":
                    imageData = loan.getPhoto();
                    logger.debug("Retrieved photo data - Length: {}", imageData != null ? imageData.length : "null");
                    break;
                case "applicant_signature":
                    imageData = loan.getApplicantSignature();
                    logger.debug("Retrieved applicant signature data - Length: {}", imageData != null ? imageData.length : "null");
                    break;
                case "guarantor_signature":
                    imageData = loan.getGuarantorSignature();
                    logger.debug("Retrieved guarantor signature data - Length: {}", imageData != null ? imageData.length : "null");
                    break;
                case "branch_seal":
                    imageData = loan.getBranchSeal();
                    logger.debug("Retrieved branch seal data - Length: {}", imageData != null ? imageData.length : "null");
                    break;
                default:
                    logger.warn("Unknown image type requested: {} for application: {}", imageType, applicationNo);
                    return ResponseEntity.notFound().build();
            }

            if (imageData == null || imageData.length == 0) {
                logger.warn("Image data is null or empty for type: {} and application: {}", imageType, applicationNo);
                return ResponseEntity.notFound().build();
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setContentLength(imageData.length);
            headers.setCacheControl("max-age=3600"); // Cache for 1 hour

            logger.info("Successfully serving image - Type: {}, Application: {}, Size: {} bytes",
                    imageType, applicationNo, imageData.length);
            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Error serving image: {} - Error: {}", imageName, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get-loan-by-acc/{accountNumber}")
    public ResponseEntity<LoanAccountsEntity> getLoanByAccountNumber(@PathVariable String accountNumber) {
        logger.info("Fetching loan by account number: {}", accountNumber);

        try {
            LoanAccountsEntity account = loanAccountsService.findByAccountNumber(accountNumber);
            logger.info("Successfully retrieved loan account: {}", accountNumber);
            return ResponseEntity.ok(account);
        } catch (Exception e) {
            logger.error("Failed to retrieve loan by account number: {} - Error: {}", accountNumber, e.getMessage(), e);
            throw e;
        }
    }

    @PutMapping("/update-loan-by-acc/{accountNumber}")
    public ResponseEntity<LoanAccountsEntity> updateLoanAccount(
            @PathVariable String accountNumber,
            @RequestPart("formData") @Valid LoanAccountsDto request,
            @RequestPart(value = "photo", required = false) MultipartFile photo,
            @RequestPart(value = "applicantSignature", required = false) MultipartFile applicantSignature,
            @RequestPart(value = "guarantorSignature", required = false) MultipartFile guarantorSignature,
            @RequestPart(value = "branchSeal", required = false) MultipartFile branchSeal
    ) throws IOException {
        logger.info("Updating loan account: {} - Application No: {}", accountNumber, request.getApplicationNo());
        logger.debug("Update files provided - Photo: {}, Applicant Signature: {}, Guarantor Signature: {}, Branch Seal: {}",
                photo != null, applicantSignature != null, guarantorSignature != null, branchSeal != null);

        try {
            LoanAccountsEntity updated = loanAccountsService.updateAccount(accountNumber, request, photo, applicantSignature, guarantorSignature, branchSeal);
            logger.info("Successfully updated loan account: {} - Application No: {}", accountNumber, updated.getApplicationNo());
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            logger.error("Failed to update loan account: {} - Error: {}", accountNumber, e.getMessage(), e);
            throw e;
        }
    }

    @PatchMapping("/patch-loan-by-acc/{accountNumber}")
    public ResponseEntity<LoanAccountsEntity> patchLoanAccount(
            @PathVariable String accountNumber,
            @RequestPart("formData") LoanAccountsDto request,
            @RequestPart(value = "photo", required = false) MultipartFile photo,
            @RequestPart(value = "applicantSignature", required = false) MultipartFile applicantSignature,
            @RequestPart(value = "guarantorSignature", required = false) MultipartFile guarantorSignature,
            @RequestPart(value = "branchSeal", required = false) MultipartFile branchSeal
    ) throws IOException {
        logger.info("Patching loan account: {} - Partial update request", accountNumber);
        logger.debug("Patch files provided - Photo: {}, Applicant Signature: {}, Guarantor Signature: {}, Branch Seal: {}",
                photo != null, applicantSignature != null, guarantorSignature != null, branchSeal != null);

        try {
            LoanAccountsEntity updated = loanAccountsService.patchAccount(accountNumber, request, photo, applicantSignature, guarantorSignature, branchSeal);
            logger.info("Successfully patched loan account: {} - Application No: {}", accountNumber, updated.getApplicationNo());
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            logger.error("Failed to patch loan account: {} - Error: {}", accountNumber, e.getMessage(), e);
            throw e;
        }
    }

    @DeleteMapping("/delete-loan-by-acc/{accountNumber}")
    public ResponseEntity<String> deleteLoanAccount(@PathVariable String accountNumber) {
        logger.info("Deleting loan account: {}", accountNumber);

        try {
            String message = loanAccountsService.delete(accountNumber);
            logger.info("Successfully deleted loan account: {} - Message: {}", accountNumber, message);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            logger.error("Failed to delete loan account: {} - Error: {}", accountNumber, e.getMessage(), e);
            throw e;
        }
    }
}
