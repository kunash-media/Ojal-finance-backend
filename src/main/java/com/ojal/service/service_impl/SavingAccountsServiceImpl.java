package com.ojal.service.service_impl;

import com.ojal.controller.LoanAccountsController;
import com.ojal.enum_accounts.AccountType;
import com.ojal.global_exception.ResourceNotFoundException;
import com.ojal.model_entity.SavingAccountsEntity;
import com.ojal.model_entity.SavingTransactionEntity;
import com.ojal.model_entity.UsersEntity;
import com.ojal.model_entity.dto.request.SavingAccountDetailsDto;
import com.ojal.model_entity.dto.request.SavingAccountUpdateDto;
import com.ojal.model_entity.dto.request.SavingAccountsDto;
import com.ojal.model_entity.dto.request.SavingTransactionDto;
import com.ojal.repository.SavingAccountsRepository;
import com.ojal.repository.SavingTransactionRepository;
import com.ojal.repository.UsersRepository;
import com.ojal.service.SavingAccountsService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class
SavingAccountsServiceImpl implements SavingAccountsService {

    private final SavingAccountsRepository savingAccountsRepository;
    private final UsersRepository userRepository;
    private final SavingTransactionRepository transactionRepository;

    private static final Logger logger = LoggerFactory.getLogger(SavingAccountsServiceImpl.class);


    @Value("${account.savings.default-minimum-balance:100}")
    private BigDecimal defaultMinimumBalance;

    @Autowired
    public SavingAccountsServiceImpl(
            SavingAccountsRepository savingAccountsRepository,
            UsersRepository userRepository,
            SavingTransactionRepository transactionRepository) {
        this.savingAccountsRepository = savingAccountsRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Transactional
    public SavingAccountsEntity createAccount(String userId, SavingAccountsDto dto) {

        log.info("Creating account for user: {}", userId);
        log.info("DTO values - interestRate: {}, minimumBalance: {}, initialDeposit: {}",
                dto.getInterestRate(), dto.getMinimumBalance(), dto.getInitialDeposit());

        UsersEntity user = userRepository.findByUserId(userId);
        boolean alreadyAccount = savingAccountsRepository.existsByUser_UserId(userId);

        if(alreadyAccount){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Account with this number already exists: "+userId);
        }

        if(user == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found with ID: "+userId);
        }

        // Validate and handle null values from DTO
        BigDecimal initialDeposit = dto.getInitialDeposit();
        BigDecimal interestRate = dto.getInterestRate();
        BigDecimal minimumBalance = dto.getMinimumBalance();

        // Handle null values
        if (initialDeposit == null) {
            throw new IllegalArgumentException("Initial deposit cannot be null");
        }
        if (interestRate == null) {
            throw new IllegalArgumentException("Interest rate cannot be null");
        }
        if (minimumBalance == null) {
            minimumBalance = defaultMinimumBalance; // Use default if not provided
        }

        // Validate initial deposit against minimum balance
        BigDecimal minimumRequired = minimumBalance.max(defaultMinimumBalance); // Use the higher value
        if (initialDeposit.compareTo(minimumRequired) < 0) {
            throw new IllegalArgumentException(
                    "Initial deposit must be at least the minimum balance amount of " + minimumRequired);
        }

        // Create new savings account
        SavingAccountsEntity account = new SavingAccountsEntity();
        account.setUser(user);
        account.setBalance(initialDeposit);
        account.setInterestRate(interestRate);
        account.setMinimumBalance(minimumRequired);

        return savingAccountsRepository.save(account);
    }

    @Override
    public SavingAccountsEntity findByAccountNumber(String accountNumber) {
        return savingAccountsRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Saving Account", "accountNumber", accountNumber));
    }

    @Override
    @Transactional(readOnly = true) // ADD THIS ANNOTATION
    public SavingAccountDetailsDto getAccountWithTransactions(String accountNumber) {
        logger.info("Fetching account details with transactions for account: {}", accountNumber);

        try {
            // Get the account
            SavingAccountsEntity account = findByAccountNumber(accountNumber);
            logger.debug("Retrieved account - ID: {}, Name: {}, Balance: {}",
                    account.getId(), account.getUser().getFirstName(), account.getBalance());

            // Get associated transactions - Same repository, should work now
            List<SavingTransactionEntity> transactions = transactionRepository
                    .findBySavingAccount_AccountNumberOrderByCreatedAtDesc(accountNumber);
            logger.debug("Retrieved {} transactions for account: {}", transactions.size(), accountNumber);

            // Debug: Log raw entities before mapping
            transactions.forEach(txn -> {
                logger.debug("Raw transaction before mapping - ID: {}, CreatedAt: '{}'",
                        txn.getId(), txn.getCreatedAt());
            });

            // Create DTO
            SavingAccountDetailsDto dto = new SavingAccountDetailsDto();
            dto.setId(account.getId());
            dto.setName(account.getUser().getFirstName());
            dto.setAccountNumber(account.getAccountNumber());
            dto.setCreatedAt(account.getCreatedAt());
            dto.setAccountType(AccountType.SAVING_AC.name());
            dto.setCurrentBalance(account.getBalance());
            dto.setInterestRate(account.getInterestRate());
            dto.setStatus(account.getStatus().name());

            logger.debug("Account DTO populated - Type: {}, Status: {}, Interest Rate: {}",
                    dto.getAccountType(), dto.getStatus(), dto.getInterestRate());

            // Map transactions to DTOs
            List<SavingTransactionDto> transactionDtos = transactions.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
            dto.setTransactionData(transactionDtos);

            logger.info("Successfully processed account details for: {} with {} transactions",
                    accountNumber, transactionDtos.size());
            return dto;

        } catch (Exception e) {
            logger.error("Failed to fetch account details with transactions for account: {} - Error: {}",
                    accountNumber, e.getMessage(), e);
            throw e;
        }
    }

    private SavingTransactionDto mapToDto(SavingTransactionEntity entity) {
        logger.debug("Mapping transaction entity to DTO - Transaction ID: {}", entity.getId());

        SavingTransactionDto dto = new SavingTransactionDto();
        dto.setId(entity.getId());

        // Log the createdAt value for debugging the null issue
        String createdAtValue = entity.getCreatedAt();
        logger.debug("Transaction ID: {} - CreatedAt value: '{}' (null: {})",
                entity.getId(), createdAtValue, createdAtValue == null);

        dto.setCreatedAt(entity.getCreatedAt());
        dto.setAmount(entity.getAmount());
        dto.setPayMode(entity.getPayMode());
        dto.setUtrNo(entity.getUtrNo());
        dto.setCash(entity.getCash());
        dto.setChequeNumber(entity.getChequeNumber());
        dto.setNote(entity.getNote());
        dto.setBalanceAfter(entity.getBalanceAfter());

        logger.debug("Mapped transaction - ID: {}, Amount: {}, PayMode: {}, BalanceAfter: {}",
                dto.getId(), dto.getAmount(), dto.getPayMode(), dto.getBalanceAfter());

        // Additional debug log specifically for the createdAt issue
        if (dto.getCreatedAt() == null) {
            logger.warn("CreatedAt is null for transaction ID: {} - This may cause display issues", dto.getId());
        }
        return dto;
    }

    /**
     * Get saving account by user ID
     * @param userId the user ID to search for
     * @return SavingAccountsEntity if found
     * @throws ResourceNotFoundException if account not found
     */
    @Override
    public SavingAccountsEntity getAccountByUserId(String userId) {
        // First check if user exists
        UsersEntity user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with ID: " + userId);
        }

        // Find the saving account for this user
        return savingAccountsRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Saving Account", "userId", userId));
    }

    /**
     * Get all saving accounts by branch name
     * @param branchName the branch name to filter by
     * @return List of SavingAccountsEntity
     * @throws IllegalArgumentException if branch name is null or empty
     */
    @Override
    public List<SavingAccountsEntity> getAllAccountsByBranch(String branchName) {
        // Validate branch name
        if (branchName == null || branchName.trim().isEmpty()) {
            throw new IllegalArgumentException("Branch name cannot be null or empty");
        }

        // Find all accounts for the specified branch
        List<SavingAccountsEntity> accounts = savingAccountsRepository.findByUser_Branch(branchName.trim());

        // Log the operation for audit purposes
        System.out.println("Retrieved " + accounts.size() + " saving accounts for branch: " + branchName);

        return accounts;
    }

    // 2. UPDATE: SavingAccountsServiceImpl.java
// REPLACE your existing deleteAccountByUserId method with this updated version

    /**
     * Delete saving account by user ID
     * @param userId the user ID
     * @return success message
     * @throws ResourceNotFoundException if account not found
     * @throws IllegalStateException if account has pending transactions or non-zero balance
     */
    @Override
    @Transactional
    public String deleteAccountByUserId(String userId) {
        // First verify the account exists
        SavingAccountsEntity account = getAccountByUserId(userId);

        // Business rule: Don't allow deletion if account has balance
        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalStateException(
                    "Cannot delete account with non-zero balance. Current balance: " + account.getBalance());
        }

        // Additional check: Verify no pending transactions (optional)
        List<SavingTransactionEntity> recentTransactions = transactionRepository
                .findBySavingAccount_AccountNumberOrderByCreatedAtDesc(account.getAccountNumber());

        // If there are recent transactions, you might want to prevent deletion
        // This is a business decision - commenting out for now
    /*
    if (!recentTransactions.isEmpty()) {
        throw new IllegalStateException(
            "Cannot delete account with transaction history. Please contact administrator.");
    }
    */

        // UPDATED DELETION LOGIC:
        // Step 1: Delete all transactions for this user's saving account first
        transactionRepository.deleteByUserUserId(userId);

        // Step 2: Now delete the saving account
        int deletedCount = savingAccountsRepository.deleteByUser_UserId(userId);

        if (deletedCount == 0) {
            throw new IllegalStateException("Failed to delete account for user: " + userId);
        }

        return "Successfully deleted saving account and all associated transactions for user: " + userId;
    }

// ADD this helper method to your service implementation class (if not already present)
// This method should be in your SavingAccountsServiceImpl class

    /**
     * Delete saving account with all its transactions by account number
     * @param accountNumber the account number
     * @return success message
     */
    @Transactional
    public String deleteAccountWithTransactions(String accountNumber) {
        // Verify account exists
        SavingAccountsEntity account = savingAccountsRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Saving Account", "accountNumber", accountNumber));

        // Business rule: Check balance
        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalStateException(
                    "Cannot delete account with non-zero balance. Current balance: " + account.getBalance());
        }

        // Delete transactions first
        transactionRepository.deleteByAccountNumber(accountNumber);

        // Delete the account
        savingAccountsRepository.delete(account);

        return "Successfully deleted saving account and all associated transactions for account: " + accountNumber;
    }

    /**
     * Update saving account by user ID (excluding account number)
     * @param userId the user ID
     * @param updateDto the update data
     * @return updated SavingAccountsEntity
     * @throws ResourceNotFoundException if account not found
     */
    @Transactional
    @Override
    public SavingAccountsEntity updateAccountByUserId(String userId, SavingAccountUpdateDto updateDto) {
        // Find existing account
        SavingAccountsEntity existingAccount = getAccountByUserId(userId);

        // Update fields (excluding account number for security)
        if (updateDto.getInterestRate() != null) {
            // Validate interest rate (should be positive and reasonable)
            if (updateDto.getInterestRate().compareTo(BigDecimal.ZERO) <= 0 ||
                    updateDto.getInterestRate().compareTo(new BigDecimal("20")) > 0) {
                throw new IllegalArgumentException("Interest rate must be between 0 and 20 percent");
            }
            existingAccount.setInterestRate(updateDto.getInterestRate());
        }

        if (updateDto.getMinimumBalance() != null) {
            // Validate minimum balance (should not be negative)
            if (updateDto.getMinimumBalance().compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Minimum balance cannot be negative");
            }

            // Check if current balance meets new minimum balance requirement
            if (existingAccount.getBalance().compareTo(updateDto.getMinimumBalance()) < 0) {
                throw new IllegalArgumentException(
                        "Cannot set minimum balance higher than current balance. Current balance: " +
                                existingAccount.getBalance() + ", Requested minimum: " + updateDto.getMinimumBalance());
            }

            existingAccount.setMinimumBalance(updateDto.getMinimumBalance());
        }

        // Add handling for initialDeposit (this updates the balance)
        if (updateDto.getInitialDeposit() != null) {
            // Validate initial deposit
            if (updateDto.getInitialDeposit().compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Initial deposit cannot be negative");
            }

            // Check if new balance meets minimum balance requirement
            if (existingAccount.getMinimumBalance() != null &&
                    updateDto.getInitialDeposit().compareTo(existingAccount.getMinimumBalance()) < 0) {
                throw new IllegalArgumentException(
                        "New balance must meet minimum balance requirement. Minimum: " +
                                existingAccount.getMinimumBalance() + ", Provided: " + updateDto.getInitialDeposit());
            }

            existingAccount.setBalance(updateDto.getInitialDeposit());
        }

        // Save and return updated account
        return savingAccountsRepository.save(existingAccount);
    }
}