package com.ojal.model_entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.security.SecureRandom;

@Entity
@Table(name = "loan_accounts_table")
public class LoanAccountsEntity extends BaseAccountEntity {

    @Column(name = "application_no")
    private String applicationNo;

    @Column(name = "purpose_of_loan")
    private String purposeOfLoan;

    @Column(name = "loan_scheme")
    private String loanScheme;

    @Column(name = "loan_amount", nullable = false)
    private BigDecimal loanAmount;

    @Column(name = "roi", nullable = false)
    private BigDecimal roi;

    @Column(name = "applied_date")
    private String appliedDate;

    @Column(name = "tenure", nullable = false)
    private Integer tenure;

    @Column(name = "emi_amount")
    private BigDecimal emiAmount;

    @Column(name = "member_name")
    private String memberName;

    @Column(name = "father_name")
    private String fatherName;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "date_of_joining")
    private String dateOfJoining;

    @Column(name = "member_type")
    private String memberType;

    @Column(name = "branch_name")
    private String branchName;

    @Column(name = "address")
    private String address;

    @Column(name = "pan_number")
    private String panNumber;

    @Column(name = "adhaar_number")
    private String adhaarNumber;

    @Column(name = "grantor_name")
    private String grantorName;

    @Column(name = "grantor_address")
    private String grantorAddress;

    @Column(name = "grantor_mobile")
    private String grantorMobile;

    @Column(name = "nominee_name")
    private String nomineeName;

    @Column(name = "nominee_address")
    private String nomineeAddress;

    @Column(name = "nominee_mobile")
    private String nomineeMobile;

    @Column(name = "processing_fee")
    private BigDecimal processingFee;

    @Column(name = "disbursed_amount")
    private BigDecimal disbursedAmount;

    @Lob
    @Column(name = "photo", columnDefinition = "LONGBLOB")
    private byte[] photo;

    @Lob
    @Column(name = "applicant_signature", columnDefinition = "LONGBLOB")
    private byte[] applicantSignature;

    @Lob
    @Column(name = "guarantor_signature", columnDefinition = "LONGBLOB")
    private byte[] guarantorSignature;

    @Lob
    @Column(name = "branch_seal", columnDefinition = "LONGBLOB")
    private byte[] branchSeal;

    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (getAccountNumber() == null) {
            setAccountNumber(generateAccountNumber());
        }
    }

    public String generateAccountNumber() {
        int currentYear = java.time.LocalDate.now().getYear();
        SecureRandom random = new SecureRandom();
        int randomNum = random.nextInt(900) + 100; // 3-digit random number
        return "LN" + currentYear + randomNum;
    }

    // Getters and setters for all fields...


    public String getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(String applicationNo) {
        this.applicationNo = applicationNo;
    }

    public String getPurposeOfLoan() {
        return purposeOfLoan;
    }

    public void setPurposeOfLoan(String purposeOfLoan) {
        this.purposeOfLoan = purposeOfLoan;
    }

    public String getLoanScheme() {
        return loanScheme;
    }

    public void setLoanScheme(String loanScheme) {
        this.loanScheme = loanScheme;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public BigDecimal getRoi() {
        return roi;
    }

    public void setRoi(BigDecimal roi) {
        this.roi = roi;
    }

    public String getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(String appliedDate) {
        this.appliedDate = appliedDate;
    }

    public Integer getTenure() {
        return tenure;
    }

    public void setTenure(Integer tenure) {
        this.tenure = tenure;
    }

    public BigDecimal getEmiAmount() {
        return emiAmount;
    }

    public void setEmiAmount(BigDecimal emiAmount) {
        this.emiAmount = emiAmount;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(String dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public String getAdhaarNumber() {
        return adhaarNumber;
    }

    public void setAdhaarNumber(String adhaarNumber) {
        this.adhaarNumber = adhaarNumber;
    }

    public String getGrantorName() {
        return grantorName;
    }

    public void setGrantorName(String grantorName) {
        this.grantorName = grantorName;
    }

    public String getGrantorAddress() {
        return grantorAddress;
    }

    public void setGrantorAddress(String grantorAddress) {
        this.grantorAddress = grantorAddress;
    }

    public String getGrantorMobile() {
        return grantorMobile;
    }

    public void setGrantorMobile(String grantorMobile) {
        this.grantorMobile = grantorMobile;
    }

    public String getNomineeName() {
        return nomineeName;
    }

    public void setNomineeName(String nomineeName) {
        this.nomineeName = nomineeName;
    }

    public String getNomineeAddress() {
        return nomineeAddress;
    }

    public void setNomineeAddress(String nomineeAddress) {
        this.nomineeAddress = nomineeAddress;
    }

    public String getNomineeMobile() {
        return nomineeMobile;
    }

    public void setNomineeMobile(String nomineeMobile) {
        this.nomineeMobile = nomineeMobile;
    }

    public BigDecimal getProcessingFee() {
        return processingFee;
    }

    public void setProcessingFee(BigDecimal processingFee) {
        this.processingFee = processingFee;
    }

    public BigDecimal getDisbursedAmount() {
        return disbursedAmount;
    }

    public void setDisbursedAmount(BigDecimal disbursedAmount) {
        this.disbursedAmount = disbursedAmount;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public byte[] getApplicantSignature() {
        return applicantSignature;
    }

    public void setApplicantSignature(byte[] applicantSignature) {
        this.applicantSignature = applicantSignature;
    }

    public byte[] getGuarantorSignature() {
        return guarantorSignature;
    }

    public void setGuarantorSignature(byte[] guarantorSignature) {
        this.guarantorSignature = guarantorSignature;
    }

    public byte[] getBranchSeal() {
        return branchSeal;
    }

    public void setBranchSeal(byte[] branchSeal) {
        this.branchSeal = branchSeal;
    }
}