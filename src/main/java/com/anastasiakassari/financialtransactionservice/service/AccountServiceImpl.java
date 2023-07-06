package com.anastasiakassari.financialtransactionservice.service;

import com.anastasiakassari.financialtransactionservice.dto.AccountDTO;
import com.anastasiakassari.financialtransactionservice.exception.*;
import com.anastasiakassari.financialtransactionservice.model.Account;
import com.anastasiakassari.financialtransactionservice.model.Transaction;
import com.anastasiakassari.financialtransactionservice.repository.AccountRepository;
import com.anastasiakassari.financialtransactionservice.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

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
        return accounts;
    }

    @Override
    public Account getAccountById(Long id) throws AccountNotFoundException {
        return accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(ExceptionMessage.ACCOUNT_NOT_FOUND + " with id: " + id));
    }

    @Override
    @Transactional
    public Account createAccount(AccountDTO dto) throws FinancialTransactionServiceException {
        //Invalid params
        if (dto.getCurrency() == null)
            throw new MissingParameterException(ExceptionMessage.MISSING_PARAMETER);
        //Invalid balance
        if (dto.getBalance() != null && dto.getBalance() < 0)
            throw new InvalidAmountException(ExceptionMessage.INVALID_AMOUNT);

        Account account = new Account();
        account.setCurrency(dto.getCurrency());
        account.setBalance(Optional.ofNullable(dto.getBalance()).orElse(0.0));
        account.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        return accountRepository.save(account);
    }

    @Override
    public Account updateAccount(Account account) throws FinancialTransactionServiceException {
        if (account.getId() == null || account.getCurrency() == null || account.getBalance() == null)
            throw new InvalidParametersException(ExceptionMessage.INVALID_PARAMETERS);
        Account dbAccount = accountRepository.findById(account.getId()).orElseThrow(() -> new AccountNotFoundException(ExceptionMessage.ACCOUNT_NOT_FOUND + " with id: " + account.getId()));
        dbAccount.setCurrency(account.getCurrency());
        if (account.getBalance() == null || account.getBalance() < 0)
            throw new InvalidAmountException(ExceptionMessage.INVALID_AMOUNT);
        dbAccount.setBalance(account.getBalance());
        return accountRepository.save(dbAccount);
    }

    @Override
    public boolean deleteAccount(Long id) throws AccountNotFoundException {
        if (accountRepository.findById(id).isEmpty())
            throw new AccountNotFoundException(ExceptionMessage.ACCOUNT_NOT_FOUND + " with id: " + id);
        try {
            accountRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Transaction> getAllTransactions(Long accountId) {
        List<Transaction> transactions = new ArrayList<>();
        transactionRepository.findAll().forEach(transactions::add);
        return transactions.stream()
                .filter(
                        transaction -> transaction.getSourceAccountId().equals(accountId) || transaction.getTargetAccountId().equals(accountId))
                .toList();
    }

    @Override
    public List<Transaction> getIncomingTransactions(Long accountId) {
        List<Transaction> transactions = new ArrayList<>();
        transactionRepository.findAll().forEach(transactions::add);
        return transactions.stream()
                .filter(
                        transaction -> transaction.getTargetAccountId().equals(accountId))
                .toList();
    }

    @Override
    public List<Transaction> getOutgoingTransactions(Long accountId) {
        List<Transaction> transactions = new ArrayList<>();
        transactionRepository.findAll().forEach(transactions::add);
        return transactions.stream()
                .filter(
                        transaction -> transaction.getSourceAccountId().equals(accountId))
                .toList();
    }
}
