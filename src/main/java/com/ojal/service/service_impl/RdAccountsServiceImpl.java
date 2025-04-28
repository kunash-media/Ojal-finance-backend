package com.ojal.service.service_impl;

import com.ojal.model_entity.BaseAccountEntity;
import com.ojal.model_entity.RdAccountsEntity;
import com.ojal.model_entity.RdAccounts_Dto.RdAccountsDto;
import com.ojal.model_entity.UsersEntity;
import com.ojal.repository.RdAccountsRepository;
import com.ojal.repository.UsersRepository;
import com.ojal.service.RdAccountsService;
import com.ojal.service.UsersService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class RdAccountsServiceImpl implements RdAccountsService {
    private final UsersRepository usersRepository;
    private final RdAccountsRepository rdAccountsRepository;
    private final UsersService userService;

    @Autowired
    public RdAccountsServiceImpl(
            UsersRepository usersRepository,
            RdAccountsRepository rdAccountsRepository,
            UsersService userService
    ) {
        this.usersRepository = usersRepository;
        this.rdAccountsRepository = rdAccountsRepository;
        this.userService = userService;
    }

    @Override
    @Transactional
    public RdAccountsEntity createAccount(String userId, RdAccountsDto request) {
        UsersEntity user = userService.findByUserId(userId);

        RdAccountsEntity rdAccount = new RdAccountsEntity();
        rdAccount.setDepositAmount(request.getDepositAmount());
        rdAccount.setInterestRate(request.getInterestRate());
        rdAccount.setTenureMonths(request.getTenureMonths());
        rdAccount.setMaturityDate(LocalDate.now().plusMonths(request.getTenureMonths()));

        // Calculate maturity amount (simplified)
        BigDecimal monthlyRate = request.getInterestRate().divide(BigDecimal.valueOf(1200), 10, RoundingMode.HALF_UP);
        BigDecimal totalDeposits = request.getDepositAmount().multiply(BigDecimal.valueOf(request.getTenureMonths()));
        BigDecimal interest = request.getDepositAmount()
                .multiply(monthlyRate)
                .multiply(BigDecimal.valueOf(request.getTenureMonths() * (request.getTenureMonths() + 1) / 2));
        rdAccount.setMaturityAmount(totalDeposits.add(interest));

        // Associate with user
        user.addRdAccount(rdAccount);

        // Save both entities
        usersRepository.save(user);
        return rdAccount;
    }

    @Override
    public RdAccountsEntity findByAccountNumber(String accountNumber) {
        return rdAccountsRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new EntityNotFoundException("RD account not found with number: " + accountNumber));
    }

    @Override
    public List<RdAccountsEntity> findAllByUserId(String userId) {
        UsersEntity user = userService.findByUserId(userId);
        return rdAccountsRepository.findByUser(user);
    }

    @Override
    @Transactional
    public RdAccountsEntity processMonthlyDeposit(String accountNumber) {
        RdAccountsEntity account = findByAccountNumber(accountNumber);

        // Implementation of monthly deposit processing
        // This is a simplified version - in production, you'd handle missed payments,
        // late payment penalties, interest adjustments, etc.

        // Here we just check if the account has matured
        if (LocalDate.now().isAfter(account.getMaturityDate())) {
            account.setStatus(BaseAccountEntity.AccountStatus.INACTIVE);
        }

        return rdAccountsRepository.save(account);
    }
}
