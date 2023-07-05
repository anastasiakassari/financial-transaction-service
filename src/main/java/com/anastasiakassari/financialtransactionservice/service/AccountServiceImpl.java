package com.anastasiakassari.financialtransactionservice.service;

import com.anastasiakassari.financialtransactionservice.dto.AccountDTO;
import com.anastasiakassari.financialtransactionservice.exception.AccountNotFoundException;
import com.anastasiakassari.financialtransactionservice.exception.InvalidAmountException;
import com.anastasiakassari.financialtransactionservice.exception.MissingParameterException;
import com.anastasiakassari.financialtransactionservice.model.Account;
import com.anastasiakassari.financialtransactionservice.model.Transaction;
import com.anastasiakassari.financialtransactionservice.repository.AccountRepository;
import com.anastasiakassari.financialtransactionservice.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        // TODO: Error NoAccountsFoundException
        return accounts;
    }

    @Override
    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElseThrow(AccountNotFoundException::new);
    }

    @Override
    @Transactional
    public Account createAccount(AccountDTO dto) {
        System.out.println("Account DTO: " + dto);
        //Invalid params
        if (dto.getCurrency() == null)
            throw new MissingParameterException();
        //Invalid balance
        if (dto.getBalance() < 0)
            throw new InvalidAmountException();

        Account account = new Account();
        account.setCurrency(dto.getCurrency());
        account.setBalance(dto.getBalance());
        account.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        System.out.println("new account:" + account);
        return accountRepository.save(new Account(account));
    }

    @Override
    public Account updateAccount(Long id, Account account) {
        // TODO: Error handling
        Account dbAccount = accountRepository.findById(id).orElseThrow(AccountNotFoundException::new);
        System.out.println(dbAccount.toString());
        dbAccount.setCurrency(account.getCurrency());
        dbAccount.setBalance(account.getBalance());
        dbAccount.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        return accountRepository.save(dbAccount);
    }

    @Override
    public boolean deleteAccount(Long id) {
        // TODO: Error handling
        try {
            accountRepository.deleteById(id);
            return true;
        } catch (IllegalArgumentException e) {

        }
        finally {
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
