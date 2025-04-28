package com.ojal.service.service_impl;

import com.ojal.model_entity.BaseAccountEntity;
import com.ojal.model_entity.LoanAccountsEntity;
import com.ojal.model_entity.LoanAccounts_Dto.LoanAccountsDto;
import com.ojal.model_entity.UsersEntity;
import com.ojal.repository.LoanAccountsRepository;
import com.ojal.repository.UsersRepository;
import com.ojal.service.LoanAccountsService;
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
public class LoanAccountsServiceImpl implements LoanAccountsService {

    private final UsersRepository usersRepository;
    private final LoanAccountsRepository loanAccountsRepository;
    private final UsersService userService;

    @Autowired
    public LoanAccountsServiceImpl(
            UsersRepository usersRepository,
            LoanAccountsRepository loanAccountsRepository,
            UsersService userService
    ) {
        this.usersRepository = usersRepository;
        this.loanAccountsRepository = loanAccountsRepository;
        this.userService = userService;
    }

    @Override
    @Transactional
    public LoanAccountsEntity createAccount(String userId, LoanAccountsDto request) {
        UsersEntity user = userService.findByUserId(userId);

        LoanAccountsEntity loanAccount = new LoanAccountsEntity();
        loanAccount.setLoanAmount(request.getLoanAmount());
        loanAccount.setInterestRate(request.getInterestRate());
        loanAccount.setTenureMonths(request.getTenureMonths());
        loanAccount.setLoanType(request.getLoanType());
        loanAccount.setDisbursementDate(LocalDate.now());
        loanAccount.setEndDate(LocalDate.now().plusMonths(request.getTenureMonths()));
        loanAccount.setOutstandingAmount(request.getLoanAmount());

        // Calculate EMI (simplified)
        double r = request.getInterestRate().doubleValue() / 12 / 100;
        double n = request.getTenureMonths().doubleValue();
        double p = request.getLoanAmount().doubleValue();
        double emi = (p * r * Math.pow(1 + r, n)) / (Math.pow(1 + r, n) - 1);
        loanAccount.setEmiAmount(BigDecimal.valueOf(emi).setScale(2, RoundingMode.HALF_UP));

        // Associate with user
        user.addLoanAccount(loanAccount);

        // Save both entities
        usersRepository.save(user);
        return loanAccount;
    }

    @Override
    public LoanAccountsEntity findByAccountNumber(String accountNumber) {
        return loanAccountsRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new EntityNotFoundException("Loan account not found with number: " + accountNumber));
    }

    @Override
    public List<LoanAccountsEntity> findAllByUserId(String userId) {
        UsersEntity user = userService.findByUserId(userId);
        return loanAccountsRepository.findByUser(user);
    }

    @Override
    @Transactional
    public LoanAccountsEntity processEmiPayment(String accountNumber) {
        LoanAccountsEntity account = findByAccountNumber(accountNumber);

        // EMI calculation and processing
        BigDecimal emiAmount = account.getEmiAmount();
        BigDecimal interestForMonth = account.getOutstandingAmount()
                .multiply(account.getInterestRate())
                .divide(BigDecimal.valueOf(1200), 10, RoundingMode.HALF_UP);
        BigDecimal principalForMonth = emiAmount.subtract(interestForMonth);

        // Update outstanding amount
        BigDecimal newOutstanding = account.getOutstandingAmount().subtract(principalForMonth);

        if (newOutstanding.compareTo(BigDecimal.ZERO) <= 0) {
            // Loan is fully paid
            account.setOutstandingAmount(BigDecimal.ZERO);
            account.setStatus(BaseAccountEntity.AccountStatus.CLOSED);
        } else {
            account.setOutstandingAmount(newOutstanding);
        }

        return loanAccountsRepository.save(account);
    }
}