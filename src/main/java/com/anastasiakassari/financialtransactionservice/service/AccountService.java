package com.anastasiakassari.financialtransactionservice.service;

import com.anastasiakassari.financialtransactionservice.dto.AccountDTO;
import com.anastasiakassari.financialtransactionservice.model.Account;
import com.anastasiakassari.financialtransactionservice.model.Transaction;

import java.util.List;

public interface AccountService {
    public List<Account> getAccounts();
    public Account getAccountById(Long id);
    public Account createAccount(AccountDTO account);
    public Account updateAccount(Account account);
    public boolean deleteAccount(Long id);
    public List<Transaction> getAllTransactions(Long accountId);
    public List<Transaction> getIncomingTransactions(Long accountId);
    public List<Transaction> getOutgoingTransactions(Long accountId);
}
