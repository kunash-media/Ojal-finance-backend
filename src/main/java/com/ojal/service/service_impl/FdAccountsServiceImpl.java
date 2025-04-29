package com.ojal.service.service_impl;

import com.ojal.model_entity.FdAccountsEntity;
import com.ojal.model_entity.dto.request.FdAccountsDto;
import com.ojal.model_entity.UsersEntity;
import com.ojal.repository.FdAccountsRepository;
import com.ojal.repository.UsersRepository;
import com.ojal.service.FdAccountsService;
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
public class FdAccountsServiceImpl implements FdAccountsService {

    private final UsersRepository usersRepository;
    private final FdAccountsRepository fdAccountsRepository;
    private final UsersService userService;

    @Autowired
    public FdAccountsServiceImpl(
            UsersRepository usersRepository,
            FdAccountsRepository fdAccountsRepository,
            UsersService userService
    ) {
        this.usersRepository = usersRepository;
        this.fdAccountsRepository = fdAccountsRepository;
        this.userService = userService;
    }

    @Override
    @Transactional
    public FdAccountsEntity createAccount(String userId, FdAccountsDto request) {
        UsersEntity user = userService.findByUserId(userId);

        FdAccountsEntity fdAccount = new FdAccountsEntity();
        fdAccount.setPrincipalAmount(request.getPrincipalAmount());
        fdAccount.setInterestRate(request.getInterestRate());
        fdAccount.setTenureDays(request.getTenureDays());
        fdAccount.setMaturityDate(LocalDate.now().plusDays(request.getTenureDays()));
        fdAccount.setAutoRenewal(request.getAutoRenewal());

        // Calculate maturity amount (simplified)
        BigDecimal annualRate = request.getInterestRate().divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
        BigDecimal timeYears = BigDecimal.valueOf(request.getTenureDays()).divide(BigDecimal.valueOf(365), 10, RoundingMode.HALF_UP);
        BigDecimal interest = request.getPrincipalAmount().multiply(annualRate).multiply(timeYears);
        fdAccount.setMaturityAmount(request.getPrincipalAmount().add(interest));

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
        UsersEntity user = userService.findByUserId(userId);
        return fdAccountsRepository.findByUser(user);
    }

//    @Override
//    @Transactional
//    public FdAccountsEntity processMaturity(String accountNumber) {
//        FdAccountsEntity account = findByAccountNumber(accountNumber);
//
//        if (LocalDate.now().isAfter(account.getMaturityDate())) {
//            if (account.getAutoRenewal()) {
//                // Create a new FD with the matured amount as principal
//                FdAccountsEntity newFd = new FdAccountsEntity();
//                newFd.setPrincipalAmount(account.getMaturityAmount());
//                newFd.setInterestRate(account.getInterestRate());
//                newFd.setTenureDays(account.getTenureDays());
//                newFd.setMaturityDate(LocalDate.now().plusDays(account.getTenureDays()));
//                newFd.setAutoRenewal(account.getAutoRenewal());
//
//                // Calculate new maturity amount
//                BigDecimal annualRate = account.getInterestRate().divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
//                BigDecimal timeYears = BigDecimal.valueOf(account.getTenureDays()).divide(BigDecimal.valueOf(365), 10, RoundingMode.HALF_UP);
//                BigDecimal interest = account.getMaturityAmount().multiply(annualRate).multiply(timeYears);
//                newFd.setMaturityAmount(account.getMaturityAmount().add(interest));
//                newFd.setUser(account.getUser());
//
//                // Mark old FD as inactive
//                account.setStatus(BaseAccountEntity.AccountStatus.INACTIVE);
//                fdAccountsRepository.save(account);
//
//                // Return the new FD
//                return fdAccountsRepository.save(newFd);
//            } else {
//                // Mark as matured but don't renew
//                account.setStatus(BaseAccountEntity.AccountStatus.INACTIVE);
//                return fdAccountsRepository.save(account);
//            }
//        }
//
//        // Account hasn't matured yet
//        return account;
//    }
}