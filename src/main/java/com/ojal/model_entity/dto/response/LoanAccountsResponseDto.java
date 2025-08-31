package com.ojal.model_entity.dto.response;

import com.ojal.model_entity.LoanAccountsEntity;

import java.math.BigDecimal;

public class LoanAccountsResponseDto {

    private Long id;
    private String applicationNo;
    private String purposeOfLoan;
    private String loanScheme;
    private BigDecimal loanAmount;
    private BigDecimal roi;
    private String appliedDate;
    private Integer tenure;
    private BigDecimal emiAmount;
    private String memberName;
    private String fatherName;
    private String mobile;
    private String dateOfJoining;
    private String memberType;
    private String branchName;
    private String address;
    private String panNumber;
    private String adhaarNumber;
    private String grantorName;
    private String grantorAddress;
    private String grantorMobile;
    private String nomineeName;
    private String nomineeAddress;
    private String nomineeMobile;
    private BigDecimal processingFee;
    private BigDecimal disbursedAmount;

    // Image URLs instead of byte arrays
    private String photoUrl;
    private String applicantSignatureUrl;
    private String guarantorSignatureUrl;
    private String branchSealUrl;

    // Include fields from BaseAccountEntity (add as needed)
    private String accountNumber;
    private String status;
    // ... other base fields

    // Constructors
    public LoanAccountsResponseDto() {}

    // Constructor to convert from Entity
    public LoanAccountsResponseDto(LoanAccountsEntity entity, String baseUrl) {
        this.id = entity.getId();
        this.applicationNo = entity.getApplicationNo();
        this.purposeOfLoan = entity.getPurposeOfLoan();
        this.loanScheme = entity.getLoanScheme();
        this.loanAmount = entity.getLoanAmount();
        this.roi = entity.getRoi();
        this.appliedDate = entity.getAppliedDate();
        this.tenure = entity.getTenure();
        this.emiAmount = entity.getEmiAmount();
        this.memberName = entity.getMemberName();
        this.fatherName = entity.getFatherName();
        this.mobile = entity.getMobile();
        this.dateOfJoining = entity.getDateOfJoining();
        this.memberType = entity.getMemberType();
        this.branchName = entity.getBranchName();
        this.address = entity.getAddress();
        this.panNumber = entity.getPanNumber();
        this.adhaarNumber = entity.getAdhaarNumber();
        this.grantorName = entity.getGrantorName();
        this.grantorAddress = entity.getGrantorAddress();
        this.grantorMobile = entity.getGrantorMobile();
        this.nomineeName = entity.getNomineeName();
        this.nomineeAddress = entity.getNomineeAddress();
        this.nomineeMobile = entity.getNomineeMobile();
        this.processingFee = entity.getProcessingFee();
        this.disbursedAmount = entity.getDisbursedAmount();

        // Set base entity fields (adjust field names based on your BaseAccountEntity)
        this.accountNumber = entity.getAccountNumber(); // Adjust getter name as needed
        // this.status = entity.getStatus(); // Add other base fields as needed

        // Generate image URLs
        this.photoUrl = entity.getPhoto() != null ? baseUrl + "/image/" + entity.getApplicationNo() + "_photo" : null;
        this.applicantSignatureUrl = entity.getApplicantSignature() != null ? baseUrl + "/image/" + entity.getApplicationNo() + "_applicant_signature" : null;
        this.guarantorSignatureUrl = entity.getGuarantorSignature() != null ? baseUrl + "/image/" + entity.getApplicationNo() + "_guarantor_signature" : null;
        this.branchSealUrl = entity.getBranchSeal() != null ? baseUrl + "/image/" + entity.getApplicationNo() + "_branch_seal" : null;
    }

    // Getters and Setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApplicationNo() { return applicationNo; }
    public void setApplicationNo(String applicationNo) { this.applicationNo = applicationNo; }

    public String getPurposeOfLoan() { return purposeOfLoan; }
    public void setPurposeOfLoan(String purposeOfLoan) { this.purposeOfLoan = purposeOfLoan; }

    public String getLoanScheme() { return loanScheme; }
    public void setLoanScheme(String loanScheme) { this.loanScheme = loanScheme; }

    public BigDecimal getLoanAmount() { return loanAmount; }
    public void setLoanAmount(BigDecimal loanAmount) { this.loanAmount = loanAmount; }

    public BigDecimal getRoi() { return roi; }
    public void setRoi(BigDecimal roi) { this.roi = roi; }

    public String getAppliedDate() { return appliedDate; }
    public void setAppliedDate(String appliedDate) { this.appliedDate = appliedDate; }

    public Integer getTenure() { return tenure; }
    public void setTenure(Integer tenure) { this.tenure = tenure; }

    public BigDecimal getEmiAmount() { return emiAmount; }
    public void setEmiAmount(BigDecimal emiAmount) { this.emiAmount = emiAmount; }

    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }

    public String getFatherName() { return fatherName; }
    public void setFatherName(String fatherName) { this.fatherName = fatherName; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getDateOfJoining() { return dateOfJoining; }
    public void setDateOfJoining(String dateOfJoining) { this.dateOfJoining = dateOfJoining; }

    public String getMemberType() { return memberType; }
    public void setMemberType(String memberType) { this.memberType = memberType; }

    public String getBranchName() { return branchName; }
    public void setBranchName(String branchName) { this.branchName = branchName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPanNumber() { return panNumber; }
    public void setPanNumber(String panNumber) { this.panNumber = panNumber; }

    public String getAdhaarNumber() { return adhaarNumber; }
    public void setAdhaarNumber(String adhaarNumber) { this.adhaarNumber = adhaarNumber; }

    public String getGrantorName() { return grantorName; }
    public void setGrantorName(String grantorName) { this.grantorName = grantorName; }

    public String getGrantorAddress() { return grantorAddress; }
    public void setGrantorAddress(String grantorAddress) { this.grantorAddress = grantorAddress; }

    public String getGrantorMobile() { return grantorMobile; }
    public void setGrantorMobile(String grantorMobile) { this.grantorMobile = grantorMobile; }

    public String getNomineeName() { return nomineeName; }
    public void setNomineeName(String nomineeName) { this.nomineeName = nomineeName; }

    public String getNomineeAddress() { return nomineeAddress; }
    public void setNomineeAddress(String nomineeAddress) { this.nomineeAddress = nomineeAddress; }

    public String getNomineeMobile() { return nomineeMobile; }
    public void setNomineeMobile(String nomineeMobile) { this.nomineeMobile = nomineeMobile; }

    public BigDecimal getProcessingFee() { return processingFee; }
    public void setProcessingFee(BigDecimal processingFee) { this.processingFee = processingFee; }

    public BigDecimal getDisbursedAmount() { return disbursedAmount; }
    public void setDisbursedAmount(BigDecimal disbursedAmount) { this.disbursedAmount = disbursedAmount; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    // Image URL getters and setters
    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    public String getApplicantSignatureUrl() { return applicantSignatureUrl; }
    public void setApplicantSignatureUrl(String applicantSignatureUrl) { this.applicantSignatureUrl = applicantSignatureUrl; }

    public String getGuarantorSignatureUrl() { return guarantorSignatureUrl; }
    public void setGuarantorSignatureUrl(String guarantorSignatureUrl) { this.guarantorSignatureUrl = guarantorSignatureUrl; }

    public String getBranchSealUrl() { return branchSealUrl; }
    public void setBranchSealUrl(String branchSealUrl) { this.branchSealUrl = branchSealUrl; }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}