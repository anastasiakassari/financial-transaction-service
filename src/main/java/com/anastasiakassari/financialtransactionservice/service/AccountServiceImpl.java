package com.anastasiakassari.financialtransactionservice.service;

import com.anastasiakassari.financialtransactionservice.dto.AccountDTO;
import com.anastasiakassari.financialtransactionservice.exception.*;
import com.anastasiakassari.financialtransactionservice.model.Account;
import com.anastasiakassari.financialtransactionservice.model.Transaction;
import com.anastasiakassari.financialtransactionservice.repository.AccountRepository;
import com.anastasiakassari.financialtransactionservice.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
    private static final String ACCOUNT_NOT_FOUND_WITH_ID = ExceptionMessage.ACCOUNT_NOT_FOUND + " with ID: ";
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<Account> getAccounts() {
        List<Account> accounts = new ArrayList<>();
        accountRepository.findAll().forEach(accounts::add);
        logger.info("Retrieved all accounts. Count: {}", accounts.size());
        return accounts;
    }

    @Override
    public Account getAccountById(Long id) throws AccountNotFoundException {
        logger.debug("Retrieving account with ID: {}", id);
        return accountRepository.findById(id)
                .orElseThrow(() -> {
                    String errorMessage = ACCOUNT_NOT_FOUND_WITH_ID + id;
                    logger.error(errorMessage);
                    return new AccountNotFoundException(errorMessage);
                });
    }

    @Override
    @Transactional
    public Account createAccount(AccountDTO dto) throws FinancialTransactionServiceException {
        logger.debug("Creating account with DTO: {}", dto);

        // Missing params
        if (dto.getCurrency() == null) {
            String errorMessage = "Currency is null";
            logger.error(errorMessage);
            throw new MissingParameterException(ExceptionMessage.MISSING_PARAMETER);
        }

        // Invalid balance
        if (dto.getBalance() != null && dto.getBalance() < 0) {
            String errorMessage = ExceptionMessage.INVALID_AMOUNT;
            logger.error(errorMessage);
            throw new InvalidAmountException(ExceptionMessage.INVALID_AMOUNT);
        }

        Account account = new Account();
        account.setCurrency(dto.getCurrency());
        account.setBalance(Optional.ofNullable(dto.getBalance()).orElse(0.0));
        account.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        account = accountRepository.save(account);
        logger.info("Account created: {}", account);
        return account;
    }

    @Override
    public Account updateAccount(Account account) throws FinancialTransactionServiceException {
        logger.debug("Updating account: {}", account);

        // Invalid params
        if (account.getId() == null || account.getCurrency() == null || account.getBalance() == null) {
            String errorMessage = ExceptionMessage.INVALID_PARAMETERS;
            logger.error(errorMessage);
            throw new InvalidParametersException(errorMessage);
        }

        // Account not found
        Account dbAccount = accountRepository.findById(account.getId())
                .orElseThrow(() -> {
                    String errorMessage = ACCOUNT_NOT_FOUND_WITH_ID + account.getId();
                    logger.error(errorMessage);
                    return new AccountNotFoundException(errorMessage);
                });

        // Invalid balance
        if (account.getBalance() == null || account.getBalance() < 0) {
            String errorMessage = ExceptionMessage.INVALID_AMOUNT + account.getBalance();
            logger.error(errorMessage);
            throw new InvalidAmountException(ExceptionMessage.INVALID_AMOUNT);
        }

        dbAccount.setCurrency(account.getCurrency());
        dbAccount.setBalance(account.getBalance());
        dbAccount = accountRepository.save(dbAccount);
        logger.info("Account updated: {}", dbAccount);
        return dbAccount;
    }

    @Override
    public boolean deleteAccount(Long id) throws AccountNotFoundException {
        logger.debug("Deleting account with ID: {}", id);

        // Account not found
        if (accountRepository.findById(id).isEmpty()) {
            String errorMessage = ACCOUNT_NOT_FOUND_WITH_ID + id;
            logger.error(errorMessage);
            throw new AccountNotFoundException(errorMessage);
        }

        try {
            accountRepository.deleteById(id);
            logger.info("Account deleted: {}", id);
            return true;
        } catch (Exception e) {
            logger.error("Error occurred while deleting account: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public List<Transaction> getAllTransactions(Long accountId) {
        logger.debug("Retrieving all transactions for account with ID: {}", accountId);
        List<Transaction> transactions = new ArrayList<>();
        transactionRepository.findAll().forEach(transactions::add);
        return transactions.stream()
                .filter(transaction -> transaction.getSourceAccountId().equals(accountId) || transaction.getTargetAccountId().equals(accountId))
                .toList();
    }

    @Override
    public List<Transaction> getIncomingTransactions(Long accountId) {
        logger.debug("Retrieving incoming transactions for account with ID: {}", accountId);
        List<Transaction> transactions = new ArrayList<>();
        transactionRepository.findAll().forEach(transactions::add);
        return transactions.stream()
                .filter(transaction -> transaction.getTargetAccountId().equals(accountId))
                .toList();
    }

    @Override
    public List<Transaction> getOutgoingTransactions(Long accountId) {
        logger.debug("Retrieving outgoing transactions for account with ID: {}", accountId);
        List<Transaction> transactions = new ArrayList<>();
        transactionRepository.findAll().forEach(transactions::add);
        return transactions.stream()
                .filter(transaction -> transaction.getSourceAccountId().equals(accountId))
                .toList();
    }
}
