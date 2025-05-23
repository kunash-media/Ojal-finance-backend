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
import java.util.List;

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
        BigDecimal maturityAmount = principal.add(interest);

        fdAccount.setMaturityAmount(maturityAmount);

        return fdAccountsRepository.save(fdAccount);
    }

    @Override
    @Transactional
    public void deleteAccount(String accountNumber) {
        FdAccountsEntity fdAccount = findByAccountNumber(accountNumber);
        fdAccountsRepository.delete(fdAccount);
    }
}