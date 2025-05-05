package com.ojal.service.service_impl;

import com.ojal.model_entity.FdAccountsEntity;
import com.ojal.model_entity.dto.request.FdAccountsDto;
import com.ojal.model_entity.UsersEntity;
import com.ojal.repository.FdAccountsRepository;
import com.ojal.repository.UsersRepository;
import com.ojal.service.FdAccountsService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;


@Service
public class FdAccountsServiceImpl implements FdAccountsService {

    private final UsersRepository usersRepository;
    private final FdAccountsRepository fdAccountsRepository;

    @Autowired
    public FdAccountsServiceImpl(
            UsersRepository usersRepository,
            FdAccountsRepository fdAccountsRepository
    ) {
        this.usersRepository = usersRepository;
        this.fdAccountsRepository = fdAccountsRepository;
    }

    @Override
    @Transactional
    public FdAccountsEntity createAccount(String userId, FdAccountsDto request) {

        UsersEntity user = usersRepository.findByUserId(userId);

        FdAccountsEntity fdAccount = new FdAccountsEntity();

        fdAccount.setPrincipalAmount(request.getPrincipalAmount());
        fdAccount.setInterestRate(request.getInterestRate());
        fdAccount.setTenureMonths(request.getTenureMonths());
        fdAccount.setMaturityDate(LocalDate.now().plusMonths(request.getTenureMonths()));

        // Calculate maturity amount using simple interest formula
        // Simple Interest = P × R × T
        // where:
        // P = Principal
        // R = Annual interest rate (in decimal)
        // T = Time (in years)
        // Maturity Amount = Principal + Simple Interest

        BigDecimal principal = request.getPrincipalAmount();
        BigDecimal rateDecimal = request.getInterestRate().divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
        BigDecimal timeInYears = BigDecimal.valueOf(request.getTenureMonths()).divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);

        // Calculate simple interest
        BigDecimal interest = principal.multiply(rateDecimal).multiply(timeInYears);

        // Calculate maturity amount
        BigDecimal maturityAmount = principal.add(interest);

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

}