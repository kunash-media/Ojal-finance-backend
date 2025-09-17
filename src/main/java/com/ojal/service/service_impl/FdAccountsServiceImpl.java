package com.ojal.service.service_impl;

import com.ojal.model_entity.BaseAccountEntity;
import com.ojal.model_entity.FdAccountsEntity;
import com.ojal.model_entity.FdTransactionEntity;
import com.ojal.model_entity.dto.request.FdAccountUpdateDto;
import com.ojal.model_entity.dto.request.FdAccountsDto;
import com.ojal.model_entity.UsersEntity;
import com.ojal.model_entity.dto.request.WithdrawRequest;
import com.ojal.repository.FdAccountsRepository;
import com.ojal.repository.FdTransactionRepository;
import com.ojal.repository.UsersRepository;
import com.ojal.service.FdAccountsService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class FdAccountsServiceImpl implements FdAccountsService {

    private final UsersRepository usersRepository;
    private final FdAccountsRepository fdAccountsRepository;
    private final FdTransactionRepository fdTransactionRepository;

    @Autowired
    public FdAccountsServiceImpl(
            UsersRepository usersRepository,
            FdAccountsRepository fdAccountsRepository,
            FdTransactionRepository fdTransactionRepository
    ) {
        this.usersRepository = usersRepository;
        this.fdAccountsRepository = fdAccountsRepository;
        this.fdTransactionRepository = fdTransactionRepository;
    }

    @Override
    @Transactional
    public FdAccountsEntity createAccount(String userId, FdAccountsDto request) {
        // Existing implementation
        UsersEntity user = usersRepository.findByUserId(userId);

        FdAccountsEntity fdAccount = new FdAccountsEntity();

        fdAccount.setPrincipalAmount(request.getPrincipalAmount());
        fdAccount.setInterestRate(request.getInterestRate());
        fdAccount.setTenureMonths(request.getTenureMonths());
        fdAccount.setMaturityDate(LocalDate.now().plusMonths(request.getTenureMonths()));

        // Calculate maturity amount using simple interest formula
        BigDecimal principal = request.getPrincipalAmount();
        BigDecimal rateDecimal = request.getInterestRate().divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
        BigDecimal timeInYears = BigDecimal.valueOf(request.getTenureMonths()).divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);

        // Calculate simple interest
        BigDecimal interest = principal.multiply(rateDecimal).multiply(timeInYears);

        // Calculate maturity amount with proper decimal scaling
        BigDecimal maturityAmount = principal.add(interest).setScale(2, RoundingMode.HALF_UP);

        fdAccount.setMaturityAmount(maturityAmount);

        // Associate with user
        user.addFdAccount(fdAccount);

        // Save both entities
        usersRepository.save(user);
        return fdAccount;
    }

    @Override
    public FdAccountsEntity findByAccountNumber(String accountNumber) {
        return fdAccountsRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new EntityNotFoundException("FD account not found with number: " + accountNumber));
    }

    @Override
    public List<FdAccountsEntity> findAllByUserId(String userId) {
        UsersEntity user = usersRepository.findByUserId(userId);
        if (user == null) {
            throw new EntityNotFoundException("User not found with ID: " + userId);
        }
        return fdAccountsRepository.findByUser(user);
    }

    @Override
    public List<FdAccountsEntity> findAllAccounts() {
        return fdAccountsRepository.findAll();
    }

    @Override
    @Transactional
    public FdAccountsEntity updateAccount(String accountNumber, FdAccountsDto request) {
        FdAccountsEntity fdAccount = findByAccountNumber(accountNumber);

        // Update fields
        fdAccount.setPrincipalAmount(request.getPrincipalAmount());
        fdAccount.setInterestRate(request.getInterestRate());
        fdAccount.setTenureMonths(request.getTenureMonths());
        fdAccount.setMaturityDate(LocalDate.now().plusMonths(request.getTenureMonths()));

        // Recalculate maturity amount
        BigDecimal principal = request.getPrincipalAmount();
        BigDecimal rateDecimal = request.getInterestRate().divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
        BigDecimal timeInYears = BigDecimal.valueOf(request.getTenureMonths()).divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);

        BigDecimal interest = principal.multiply(rateDecimal).multiply(timeInYears);
        BigDecimal maturityAmount = principal.add(interest).setScale(2, RoundingMode.HALF_UP);

        fdAccount.setMaturityAmount(maturityAmount);

        return fdAccountsRepository.save(fdAccount);
    }

    @Override
    @Transactional
    public void deleteAccount(String accountNumber) {
        FdAccountsEntity fdAccount = findByAccountNumber(accountNumber);
        fdAccountsRepository.delete(fdAccount);
    }

    // ------------ NEW METHODS MATCHING RD PATTERN -----------//
    @Override
    @Transactional
    public FdAccountsEntity updateFdAccountPartial(String accountNumber, FdAccountUpdateDto updateRequest) {
        FdAccountsEntity account = findByAccountNumber(accountNumber);

        // Update only provided fields (null-safe updates)
        if (updateRequest.getPrincipalAmount() != null) {
            account.setPrincipalAmount(updateRequest.getPrincipalAmount());
        }

        if (updateRequest.getInterestRate() != null) {
            account.setInterestRate(updateRequest.getInterestRate());
        }

        if (updateRequest.getTenureMonths() != null) {
            account.setTenureMonths(updateRequest.getTenureMonths());
            // Recalculate maturity date
            account.setMaturityDate(LocalDate.now().plusMonths(updateRequest.getTenureMonths()));
        }

        // Recalculate maturity amount if any relevant field was updated
        if (updateRequest.getPrincipalAmount() != null ||
                updateRequest.getInterestRate() != null ||
                updateRequest.getTenureMonths() != null) {

            recalculateMaturityAmount(account);
        }

        return fdAccountsRepository.save(account);
    }

    @Override
    @Transactional
    public void deleteByAccountNumber(String accountNumber) {
        FdAccountsEntity account = findByAccountNumber(accountNumber);
        fdAccountsRepository.delete(account);
    }


    @Override
    @Transactional
    public int deleteAllByUserId(String userId) {
        UsersEntity user = usersRepository.findByUserId(userId);
        List<FdAccountsEntity> fdAccounts = fdAccountsRepository.findByUser(user);
        int deletedCount = fdAccounts.size();
        fdAccountsRepository.deleteAll(fdAccounts);
        return deletedCount;
    }

    // Helper method to recalculate maturity amount with proper decimal scaling
    private void recalculateMaturityAmount(FdAccountsEntity account) {
        BigDecimal principal = account.getPrincipalAmount();
        BigDecimal rateDecimal = account.getInterestRate().divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
        BigDecimal timeInYears = BigDecimal.valueOf(account.getTenureMonths()).divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);

        BigDecimal interest = principal.multiply(rateDecimal).multiply(timeInYears);
        BigDecimal maturityAmount = principal.add(interest).setScale(2, RoundingMode.HALF_UP);

        account.setMaturityAmount(maturityAmount);
    }

    @Override
    @Transactional
    public FdAccountsEntity withdrawFd(String accountNumber, WithdrawRequest request) {
        FdAccountsEntity account = findByAccountNumber(accountNumber);

        // if already withdrawn or closed
        if (Boolean.TRUE.equals(account.getIsWithdrawn()) || account.getStatus() != BaseAccountEntity.AccountStatus.ACTIVE) {
            throw new IllegalStateException("FD already closed/withdrawn or not active: " + accountNumber);
        }

        LocalDate today = LocalDate.now();
        LocalDate maturity = account.getMaturityDate();

        BigDecimal principal = account.getPrincipalAmount();
        BigDecimal originalRate = account.getInterestRate(); // e.g., 7.0

        // default values
        BigDecimal payout = BigDecimal.ZERO;
        BigDecimal penaltyApplied = BigDecimal.ZERO;

        // parse createdAt to compute elapsed time if needed
        // createdAt stored in BaseAccountEntity as "yyyy-MM-dd hh:mm a"
        LocalDateTime createdDateTime = null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
            createdDateTime = LocalDateTime.parse(account.getCreatedAt(), formatter);
        } catch (Exception e) {
            // fallback: use maturity - tenureMonths to estimate created date
            createdDateTime = LocalDateTime.now().minusMonths(account.getTenureMonths());
        }

        // Time elapsed in months (approx) between creation and now
        long monthsElapsed = ChronoUnit.MONTHS.between(createdDateTime.toLocalDate().withDayOfMonth(1),
                today.withDayOfMonth(1));
        if (monthsElapsed < 0) monthsElapsed = 0;

        // time in years for interest calculation
        BigDecimal timeInYearsElapsed = BigDecimal.valueOf(monthsElapsed).divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);

        if (today.isBefore(maturity)) {
            // Premature withdrawal -> penalty: reduce interest rate by 3 percentage points
            BigDecimal penaltyPercent = BigDecimal.valueOf(3);
            BigDecimal adjustedRate = originalRate.subtract(penaltyPercent);
            if (adjustedRate.compareTo(BigDecimal.ZERO) < 0) {
                adjustedRate = BigDecimal.ZERO;
            }

            BigDecimal adjustedRateDecimal = adjustedRate.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);

            // interest earned only for elapsed months (not full tenure)
            BigDecimal interestEarned = principal.multiply(adjustedRateDecimal).multiply(timeInYearsElapsed);

            payout = principal.add(interestEarned).setScale(2, RoundingMode.HALF_UP);

            // penaltyApplied as difference between interest that would have been earned (without penalty) for elapsed period
            BigDecimal originalRateDecimal = originalRate.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
            BigDecimal originalInterestForElapsed = principal.multiply(originalRateDecimal).multiply(timeInYearsElapsed);

            penaltyApplied = originalInterestForElapsed.subtract(interestEarned).setScale(2, RoundingMode.HALF_UP);
        } else {
            // On or after maturity -> full maturity amount
            payout = account.getMaturityAmount() == null ? principal : account.getMaturityAmount().setScale(2, RoundingMode.HALF_UP);
            penaltyApplied = BigDecimal.ZERO;
        }

        // set fields and close FD
        account.setWithdrawnDate(today);
        account.setPenaltyApplied(penaltyApplied);
        account.setPayoutAmount(payout);
        account.setIsWithdrawn(true);
        account.setStatus(BaseAccountEntity.AccountStatus.CLOSED); // requires AccountStatus enum has CLOSED

        // Optionally update balance
        account.setBalance(payout);

        // Save a transaction record if your flow uses fdTransactionRepository
//        try {
//            FdTransactionEntity tx = new FdTransactionEntity();
//            tx.setFdAccount(account);
//            tx.setTransactionType("WITHDRAW");
//            tx.setAmount(payout);
//            tx.setTransactionDate(LocalDateTime.now());
//            tx.setRemarks(request != null ? request.getReason() : "Premature/Normal withdraw");
//            fdTransactionRepository.save(tx);
//        } catch (Exception e) {
//            // If you don't have FdTransactionEntity or repo, skip safely
//        }


        return fdAccountsRepository.save(account);
    }

}