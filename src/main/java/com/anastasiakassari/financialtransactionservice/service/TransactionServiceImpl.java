package com.anastasiakassari.financialtransactionservice.service;

import com.anastasiakassari.financialtransactionservice.dto.TransactionDTO;
import com.anastasiakassari.financialtransactionservice.exception.*;
import com.anastasiakassari.financialtransactionservice.model.Account;
import com.anastasiakassari.financialtransactionservice.model.Currency;
import com.anastasiakassari.financialtransactionservice.model.Transaction;
import com.anastasiakassari.financialtransactionservice.repository.AccountRepository;
import com.anastasiakassari.financialtransactionservice.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public List<Transaction> getTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        transactionRepository.findAll().forEach(transactions::add);
        logger.info("Retrieved all transactions. Count: {}", transactions.size());
        return transactions;
    }

    @Override
    public Transaction getTransactionById(Long id) throws TransactionNotFoundException {
        logger.debug("Retrieving transaction with ID: {}", id);
        return transactionRepository.findById(id)
                .orElseThrow(() -> {
                    String errorMessage = ExceptionMessage.TRANSACTION_NOT_FOUND + " with ID: " + id;
                    logger.error(errorMessage);
                    return new TransactionNotFoundException(errorMessage);
                });
    }

    @Override
    @Transactional
    public Transaction createTransaction(TransactionDTO dto) throws FinancialTransactionServiceException {
        logger.debug("Creating transaction with DTO: {}", dto);

        // Invalid params
        if (dto.getSourceAccountId() == null || dto.getTargetAccountId() == null || dto.getAmount() == null || dto.getCurrency() == null) {
            String errorMessage = ExceptionMessage.MISSING_PARAMETER;
            logger.error(errorMessage);
            throw new MissingParameterException(errorMessage);
        }

        long sourceId = dto.getSourceAccountId();
        long targetId = dto.getTargetAccountId();
        double amount = dto.getAmount();
        Currency currency = dto.getCurrency();

        // Account(s) not found
        Account sourceAccount = accountRepository.findById(sourceId).orElseThrow(() -> {
            String errorMessage = ExceptionMessage.ACCOUNT_NOT_FOUND + " with ID: " + sourceId;
            logger.error(errorMessage);
            return new AccountNotFoundException(errorMessage);
        });
        Account targetAccount = accountRepository.findById(targetId).orElseThrow(() -> {
            String errorMessage = ExceptionMessage.ACCOUNT_NOT_FOUND + " with ID: " + targetId;
            logger.error(errorMessage);
            return new AccountNotFoundException(errorMessage);
        });
        logger.debug("SourceAccount: {}", sourceAccount);
        logger.debug("TargetAccount: {}", targetAccount);

        // Same account
        if (sourceAccount.equals(targetAccount)) {
            String errorMessage = ExceptionMessage.SAME_ACCOUNT;
            logger.error(errorMessage);
            throw new SameAccountException(errorMessage);
        }

        // Invalid amount
        if (amount <= 0) {
            String errorMessage = ExceptionMessage.INVALID_AMOUNT + amount;
            logger.error(errorMessage);
            throw new InvalidAmountException(ExceptionMessage.INVALID_AMOUNT);
        }

        // Invalid currency
        if (!sourceAccount.getCurrency().equals(currency) || !targetAccount.getCurrency().equals(currency)) {
            String errorMessage = ExceptionMessage.INVALID_CURRENCY + currency;
            logger.error(errorMessage);
            throw new InvalidCurrencyException(ExceptionMessage.INVALID_CURRENCY);
        }

        // Insufficient balance
        double currentBalanceSource = sourceAccount.getBalance();
        if (currentBalanceSource < amount) {
            String errorMessage = ExceptionMessage.INSUFFICIENT_BALANCE + currentBalanceSource + " < " + amount;
            logger.error(errorMessage);
            throw new InsufficientBalanceException(ExceptionMessage.INSUFFICIENT_BALANCE);
        }
        double currentBalanceTarget = targetAccount.getBalance();

        Transaction transaction = new Transaction();
        transaction.setSourceAccountId(sourceId);
        transaction.setTargetAccountId(targetId);
        transaction.setAmount(amount);
        transaction.setCurrency(currency);
        logger.debug("Transaction: {}", transaction);

        // Update accounts
        sourceAccount.setBalance(currentBalanceSource - amount);
        targetAccount.setBalance(currentBalanceTarget + amount);
        logger.debug("Updated accounts: {}, {}", sourceAccount, targetAccount);

        accountRepository.save(sourceAccount);
        accountRepository.save(targetAccount);
        transaction = transactionRepository.save(transaction);
        logger.info("Transaction created: {}", transaction);
        return transaction;
    }
}
