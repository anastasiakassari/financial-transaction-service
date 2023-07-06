package com.anastasiakassari.financialtransactionservice.service;

import com.anastasiakassari.financialtransactionservice.dto.AccountDTO;
import com.anastasiakassari.financialtransactionservice.model.Account;
import com.anastasiakassari.financialtransactionservice.model.Transaction;

import java.util.List;

public interface AccountService {
    List<Account> getAccounts();

    Account getAccountById(Long id);

    Account createAccount(AccountDTO account);

    Account updateAccount(Account account);

    boolean deleteAccount(Long id);

    List<Transaction> getAllTransactions(Long accountId);

    List<Transaction> getIncomingTransactions(Long accountId);

    List<Transaction> getOutgoingTransactions(Long accountId);
}
