package com.anastasiakassari.financialtransactionservice.service;

import com.anastasiakassari.financialtransactionservice.dto.TransactionDTO;
import com.anastasiakassari.financialtransactionservice.exception.*;
import com.anastasiakassari.financialtransactionservice.model.Account;
import com.anastasiakassari.financialtransactionservice.model.Currency;
import com.anastasiakassari.financialtransactionservice.model.Transaction;
import com.anastasiakassari.financialtransactionservice.repository.AccountRepository;
import com.anastasiakassari.financialtransactionservice.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

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
        return transactions;
    }

    @Override
    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id).orElseThrow(TransactionNotFoundException::new);
    }

    @Override
    @Transactional
    public Transaction createTransaction(TransactionDTO dto) {
        //Invalid params
        if(dto.getSourceAccountId() == null || dto.getTargetAccountId() == null || dto.getAmount() == null || dto.getCurrency() == null)
            throw new MissingParameterException();

        System.out.println("Transaction dto: "+dto);

        long sourceId = dto.getSourceAccountId();
        long targetId = dto.getTargetAccountId();
        double amount = dto.getAmount();
        Currency currency = dto.getCurrency();

        Account sourceAccount = accountRepository.findById(sourceId).orElseThrow(AccountNotFoundException::new);
        Account targetAccount = accountRepository.findById(targetId).orElseThrow(AccountNotFoundException::new);

        System.out.println("Source account: "+sourceAccount);
        System.out.println("Target account: "+targetAccount);

        //Same account
        if (sourceAccount.equals(targetAccount))
            throw new SameAccountException();

        //Invalid amount
        if (amount < 0)
            throw new InvalidAmountException();

        //Invalid currency
        if (!sourceAccount.getCurrency().equals(currency) || !targetAccount.getCurrency().equals(currency) || !sourceAccount.getCurrency().equals(targetAccount.getCurrency()))
            throw new InvalidCurrencyException();

        //Insufficient balance
        double currentBalanceSource = sourceAccount.getBalance();
        if (currentBalanceSource < amount || currentBalanceSource-amount < 0)
            throw new InsufficientBalanceException();
        double currentBalanceTarget = targetAccount.getBalance();

        Transaction transaction = new Transaction();
        transaction.setSourceAccountId(sourceId);
        transaction.setTargetAccountId(targetId);
        transaction.setAmount(amount);
        transaction.setCurrency(currency);

        System.out.println("Transaction: "+transaction);

        // Update accounts
        sourceAccount.setBalance(currentBalanceSource-amount);
        targetAccount.setBalance(currentBalanceTarget+amount);

        accountRepository.save(sourceAccount);
        accountRepository.save(targetAccount);
        return transactionRepository.save(transaction);
    }

}
