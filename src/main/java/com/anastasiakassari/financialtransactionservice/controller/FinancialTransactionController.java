package com.anastasiakassari.financialtransactionservice.controller;

import com.anastasiakassari.financialtransactionservice.dto.AccountDTO;
import com.anastasiakassari.financialtransactionservice.dto.TransactionDTO;
import com.anastasiakassari.financialtransactionservice.exception.FinancialTransactionServiceException;
import com.anastasiakassari.financialtransactionservice.model.Account;
import com.anastasiakassari.financialtransactionservice.model.Transaction;
import com.anastasiakassari.financialtransactionservice.service.AccountService;
import com.anastasiakassari.financialtransactionservice.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fts")
public class FinancialTransactionController {

    private final AccountService accountService;
    private final TransactionService transactionService;


    @Autowired
    public FinancialTransactionController(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @GetMapping("/accounts")
    public List<Account> getAccounts() {
        return accountService.getAccounts();
    }

    @GetMapping("/account/{id}")
    public Account getAccountById(@PathVariable Long id) {
        return accountService.getAccountById(id);
    }

    @PostMapping("/account")
    @ResponseStatus(HttpStatus.CREATED)
    public Account createAccount(@RequestBody AccountDTO account) throws FinancialTransactionServiceException {
        return accountService.createAccount(account);
    }

    @GetMapping("/transactions")
    public List<Transaction> getTransactions() {
        return transactionService.getTransactions();
    }

    @GetMapping("/transaction/{id}")
    public Transaction getTransactionById(@PathVariable Long id) {
        return transactionService.getTransactionById(id);
    }

    @PostMapping("/transaction")
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction createTransaction(@RequestBody TransactionDTO transaction) throws FinancialTransactionServiceException {
        return transactionService.createTransaction(transaction);
    }

}
