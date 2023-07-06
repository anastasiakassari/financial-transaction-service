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
    public Transaction getTransactionById(Long id) throws TransactionNotFoundException {
        return transactionRepository.findById(id).orElseThrow(() -> new TransactionNotFoundException(ExceptionMessage.TRANSACTION_NOT_FOUND + " with id: " + id));
    }

    @Override
    @Transactional
    public Transaction createTransaction(TransactionDTO dto) throws FinancialTransactionServiceException {
        //Invalid params
        if (dto.getSourceAccountId() == null || dto.getTargetAccountId() == null || dto.getAmount() == null || dto.getCurrency() == null)
            throw new MissingParameterException(ExceptionMessage.MISSING_PARAMETER);

        long sourceId = dto.getSourceAccountId();
        long targetId = dto.getTargetAccountId();
        double amount = dto.getAmount();
        Currency currency = dto.getCurrency();

        Account sourceAccount = accountRepository.findById(sourceId).orElseThrow(() -> new AccountNotFoundException(ExceptionMessage.ACCOUNT_NOT_FOUND + " with id: " + sourceId));
        Account targetAccount = accountRepository.findById(targetId).orElseThrow(() -> new AccountNotFoundException(ExceptionMessage.ACCOUNT_NOT_FOUND + " with id: " + targetId));

        //Same account
        if (sourceAccount.equals(targetAccount))
            throw new SameAccountException(ExceptionMessage.SAME_ACCOUNT);

        //Invalid amount
        if (amount <= 0)
            throw new InvalidAmountException(ExceptionMessage.INVALID_AMOUNT);

        //Invalid currency
        if (!sourceAccount.getCurrency().equals(currency) || !targetAccount.getCurrency().equals(currency))
            throw new InvalidCurrencyException(ExceptionMessage.INVALID_CURRENCY);

        //Insufficient balance
        double currentBalanceSource = sourceAccount.getBalance();
        if (currentBalanceSource < amount)
            throw new InsufficientBalanceException(ExceptionMessage.INSUFFICIENT_BALANCE);
        double currentBalanceTarget = targetAccount.getBalance();

        Transaction transaction = new Transaction();
        transaction.setSourceAccountId(sourceId);
        transaction.setTargetAccountId(targetId);
        transaction.setAmount(amount);
        transaction.setCurrency(currency);

        // Update accounts
        sourceAccount.setBalance(currentBalanceSource - amount);
        targetAccount.setBalance(currentBalanceTarget + amount);

        accountRepository.save(sourceAccount);
        accountRepository.save(targetAccount);
        return transactionRepository.save(transaction);
    }

}
