package com.ojal.service.service_impl;

import com.ojal.model_entity.BaseAccountEntity;
import com.ojal.model_entity.RdAccountsEntity;
import com.ojal.model_entity.dto.request.RdAccountUpdateDto;
import com.ojal.model_entity.dto.request.RdAccountsDto;
import com.ojal.model_entity.UsersEntity;
import com.ojal.repository.RdAccountsRepository;
import com.ojal.repository.UsersRepository;
import com.ojal.service.RdAccountsService;
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
public class RdAccountsServiceImpl implements RdAccountsService {
    private final UsersRepository usersRepository;
    private final RdAccountsRepository rdAccountsRepository;

    @Autowired
    public RdAccountsServiceImpl(
            UsersRepository usersRepository,
            RdAccountsRepository rdAccountsRepository) {
        this.usersRepository = usersRepository;
        this.rdAccountsRepository = rdAccountsRepository;
    }


    @Override
    @Transactional
    public UsersEntity createAccount(String userId, RdAccountsDto request) {
        // 1. Find the user
        UsersEntity user = usersRepository.findByUserId(userId);

        // 2. Create RD Account
        RdAccountsEntity rdAccount = new RdAccountsEntity();
        rdAccount.setDepositAmount(request.getDepositAmount());
        rdAccount.setInterestRate(request.getInterestRate());
        rdAccount.setTenureMonths(request.getTenureMonths());
        rdAccount.setMaturityDate(LocalDate.now().plusMonths(request.getTenureMonths()));


        // 3. Calculate Maturity Amount (same as your original logic)
        BigDecimal monthlyRate = request.getInterestRate().divide(BigDecimal.valueOf(1200), 10, RoundingMode.HALF_UP);

        BigDecimal totalDeposits = request.getDepositAmount().multiply(BigDecimal.valueOf(request.getTenureMonths()));
        BigDecimal interest = request.getDepositAmount()
                .multiply(monthlyRate)
                .multiply(BigDecimal.valueOf(request.getTenureMonths() * (request.getTenureMonths() + 1) / 2));

        //rdAccount.setMaturityAmount(totalDeposits.add(interest));

        // Round final maturity amount to 2 decimal places
         BigDecimal maturityAmount = totalDeposits.add(interest).setScale(2, RoundingMode.HALF_UP);
         rdAccount.setMaturityAmount(maturityAmount);

        // 4. Associate RD Account with User
        user.addRdAccount(rdAccount);

        // 5. Save (JPA automatically cascades if properly mapped)
        return usersRepository.save(user); // Returns updated user with new RD account
    }



    @Override
    public RdAccountsEntity findByAccountNumber(String accountNumber) {
        return rdAccountsRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new EntityNotFoundException("RD account not found with number: " + accountNumber));
    }

    @Override
    public List<RdAccountsEntity> findAllByUserId(String userId) {
        UsersEntity user = usersRepository.findByUserId(userId);
        return rdAccountsRepository.findByUser(user);
    }


    // ------------ NEW PATCH METHOD IMPLEMENTATION -----------//
    @Override
    @Transactional
    public RdAccountsEntity updateRdAccount(String accountNumber, RdAccountUpdateDto updateRequest) {
        RdAccountsEntity account = findByAccountNumber(accountNumber);

        // Update only provided fields (null-safe updates)
        if (updateRequest.getDepositAmount() != null) {
            account.setDepositAmount(updateRequest.getDepositAmount());
        }

        if (updateRequest.getInterestRate() != null) {
            account.setInterestRate(updateRequest.getInterestRate());
        }

        if (updateRequest.getTenureMonths() != null) {
            account.setTenureMonths(updateRequest.getTenureMonths());
            // Recalculate maturity date based on creation date
            account.setMaturityDate(LocalDate.now().plusMonths(updateRequest.getTenureMonths()));
        }

        // Recalculate maturity amount if any relevant field was updated
        if (updateRequest.getDepositAmount() != null ||
                updateRequest.getInterestRate() != null ||
                updateRequest.getTenureMonths() != null) {

            recalculateMaturityAmount(account);
        }

        return rdAccountsRepository.save(account);
    }

    // Helper method to recalculate maturity amount
    private void recalculateMaturityAmount(RdAccountsEntity account) {
        BigDecimal monthlyRate = account.getInterestRate()
                .divide(BigDecimal.valueOf(1200), 10, RoundingMode.HALF_UP);

        BigDecimal totalDeposits = account.getDepositAmount()
                .multiply(BigDecimal.valueOf(account.getTenureMonths()));


        BigDecimal interest = account.getDepositAmount()
                .multiply(monthlyRate)
                .multiply(BigDecimal.valueOf(account.getTenureMonths() * (account.getTenureMonths() + 1) / 2));

        // account.setMaturityAmount(totalDeposits.add(interest));
        // Round final maturity amount to 2 decimal places
        BigDecimal maturityAmount = totalDeposits.add(interest).setScale(2, RoundingMode.HALF_UP);
        account.setMaturityAmount(maturityAmount);
    }

    // ------------ DELETE METHODS IMPLEMENTATION -----------//
    @Override
    @Transactional
    public void deleteByAccountNumber(String accountNumber) {
        RdAccountsEntity account = findByAccountNumber(accountNumber);
        rdAccountsRepository.delete(account);
    }

    @Override
    @Transactional
    public int deleteAllByUserId(String userId) {
        UsersEntity user = usersRepository.findByUserId(userId);
        List<RdAccountsEntity> rdAccounts = rdAccountsRepository.findByUser(user);
        int deletedCount = rdAccounts.size();
        rdAccountsRepository.deleteAll(rdAccounts);
        return deletedCount;
    }


    @Override
    @Transactional
    public RdAccountsEntity processMonthlyDeposit(String accountNumber) {
        RdAccountsEntity account = findByAccountNumber(accountNumber);

        // Implementation of monthly deposit processing
        // late payment penalties, interest adjustments, etc.

        // Here we just check if the account has matured
        if (LocalDate.now().isAfter(account.getMaturityDate())) {
            account.setStatus(BaseAccountEntity.AccountStatus.INACTIVE);
        }

        return rdAccountsRepository.save(account);
    }

    @Override
    @Transactional
    public RdAccountsEntity withdrawRdAccount(String accountNumber) {
        RdAccountsEntity account = findByAccountNumber(accountNumber);

        LocalDate today = LocalDate.now();

        // Parse createdAt string into LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
        LocalDateTime createdDateTime = LocalDateTime.parse(account.getCreatedAt(), formatter);
        LocalDate createdDate = createdDateTime.toLocalDate();

        // Calculate how many months completed
        int monthsCompleted = Math.min(
                account.getTenureMonths(),
                (int) ChronoUnit.MONTHS.between(createdDate, today)
        );
        if (monthsCompleted == 0) {
            monthsCompleted = 1; // ensure at least 1 installment
        }

        BigDecimal actualDeposits = account.getDepositAmount()
                .multiply(BigDecimal.valueOf(monthsCompleted));

        BigDecimal totalPlannedDeposits = account.getDepositAmount()
                .multiply(BigDecimal.valueOf(account.getTenureMonths()));

        BigDecimal interest = BigDecimal.ZERO;
        BigDecimal penalty = BigDecimal.ZERO;
        BigDecimal payout;

        // -------------------------
        // Case 1: Withdraw before 3 months → only deposits
        // -------------------------
        if (monthsCompleted < 3) {
            payout = actualDeposits;
        }

        // -------------------------
        // Case 2: Premature withdrawal → deposits + penalized interest
        // -------------------------
        else if (today.isBefore(account.getMaturityDate())) {
            BigDecimal monthlyRate = account.getInterestRate()
                    .divide(BigDecimal.valueOf(1200), 10, RoundingMode.HALF_UP);

            BigDecimal normalInterest = account.getDepositAmount()
                    .multiply(monthlyRate)
                    .multiply(BigDecimal.valueOf(monthsCompleted * (monthsCompleted + 1) / 2));

            BigDecimal effectiveRate = account.getInterestRate().subtract(BigDecimal.valueOf(3));
            if (effectiveRate.compareTo(BigDecimal.ZERO) < 0) {
                effectiveRate = BigDecimal.ZERO;
            }

            BigDecimal effectiveMonthlyRate = effectiveRate.divide(BigDecimal.valueOf(1200), 10, RoundingMode.HALF_UP);
            BigDecimal penalizedInterest = account.getDepositAmount()
                    .multiply(effectiveMonthlyRate)
                    .multiply(BigDecimal.valueOf(monthsCompleted * (monthsCompleted + 1) / 2));

            interest = penalizedInterest;
            penalty = normalInterest.subtract(penalizedInterest);

            payout = actualDeposits.add(interest);
        }

        // -------------------------
        // Case 3: On or after maturity → full maturity
        // -------------------------
        else {
            payout = account.getMaturityAmount(); // use projected maturity from creation
            interest = payout.subtract(totalPlannedDeposits);
        }

        // -------------------------
        // Update entity only with withdrawal values
        // -------------------------
        account.setInterestEarned(interest.setScale(2, RoundingMode.HALF_UP));
        account.setPenaltyApplied(penalty.setScale(2, RoundingMode.HALF_UP));
        account.setPayoutAmount(payout.setScale(2, RoundingMode.HALF_UP)); // ✅ fixed: only actual withdrawn
        account.setWithdrawnDate(today);
        account.setStatus(BaseAccountEntity.AccountStatus.CLOSED);

        return rdAccountsRepository.save(account);
    }


}
