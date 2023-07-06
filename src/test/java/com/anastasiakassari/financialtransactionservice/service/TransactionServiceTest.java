package com.anastasiakassari.financialtransactionservice.service;

import com.anastasiakassari.financialtransactionservice.dto.TransactionDTO;
import com.anastasiakassari.financialtransactionservice.exception.*;
import com.anastasiakassari.financialtransactionservice.model.Account;
import com.anastasiakassari.financialtransactionservice.model.Currency;
import com.anastasiakassari.financialtransactionservice.model.Transaction;
import com.anastasiakassari.financialtransactionservice.repository.AccountRepository;
import com.anastasiakassari.financialtransactionservice.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class TransactionServiceTest {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        accountRepository.save(new Account(1L, 100.0, Currency.EUR, new Timestamp(System.currentTimeMillis())));
        accountRepository.save(new Account(2L, 0.0, Currency.EUR, new Timestamp(System.currentTimeMillis())));
        accountRepository.save(new Account(3L, 100.0, Currency.GBP, new Timestamp(System.currentTimeMillis())));
    }

    @Test
    void getTransactions() {
        List<Transaction> transactions = transactionService.getTransactions();
        assertNotNull(transactions);
        int initialSize = transactions.size();

        Transaction transaction = transactionRepository.save(
                new Transaction(null, 1L, 2L, 1.0, Currency.EUR));

        transactions = transactionService.getTransactions();
        Optional<Transaction> optional = transactions.stream().filter(a -> a.getId().equals(transaction.getId())).findFirst();
        assertTrue(optional.isPresent());
        Transaction serviceTransaction = optional.get();
        assertEquals(transaction, serviceTransaction);
        assertEquals(initialSize + 1, transactions.size());
    }

    @Test
    void getTransactionById() {
        Transaction transaction = transactionRepository.save(
                new Transaction(null, 1L, 2L, 1.0, Currency.EUR));

        Transaction serviceTransaction = transactionService.getTransactionById(transaction.getId());
        assertNotNull(serviceTransaction);
        assertEquals(transaction, serviceTransaction);
    }

    @Test
    void failToGetTransactionWithInvalidId() {
        long invalidId = -1L;
        var exception = assertThrows(TransactionNotFoundException.class, () -> transactionService.getTransactionById(invalidId));
        assertEquals(ExceptionMessage.TRANSACTION_NOT_FOUND + " with id: " + invalidId, exception.getMessage());
    }

    @Test
    void createTransactionWithAllParams() {
        long sourceId = 1L;
        long targetId = 2L;
        double amount = 1.0;

        Optional<Account> sourceOptional = accountRepository.findById(sourceId);
        assertTrue(sourceOptional.isPresent());
        double sourceAccountBalance = sourceOptional.get().getBalance();
        Optional<Account> targetOptional = accountRepository.findById(targetId);
        assertTrue(targetOptional.isPresent());
        double targetAccountBalance = targetOptional.get().getBalance();

        TransactionDTO dto = new TransactionDTO();
        dto.setSourceAccountId(sourceId);
        dto.setTargetAccountId(targetId);
        dto.setAmount(amount);
        dto.setCurrency(Currency.EUR);

        Transaction transaction = transactionService.createTransaction(dto);

        assertNotNull(transaction);
        assertEquals(sourceId, transaction.getSourceAccountId(), "Incorrect source account ID");
        assertEquals(targetId, transaction.getTargetAccountId(), "Incorrect target account ID");
        assertEquals(amount, transaction.getAmount(), "Incorrect transaction amount");
        assertEquals(Currency.EUR, transaction.getCurrency(), "Incorrect transaction currency");

        sourceOptional = accountRepository.findById(sourceId);
        assertTrue(sourceOptional.isPresent());
        targetOptional = accountRepository.findById(targetId);
        assertTrue(targetOptional.isPresent());
        assertEquals(sourceAccountBalance - amount, sourceOptional.get().getBalance(), "Incorrect source account balance");
        assertEquals(targetAccountBalance + amount, targetOptional.get().getBalance(), "Incorrect target account balance");
    }

    @Test
    void failToCreateTransactionWithMissingParams() {
        TransactionDTO dto = new TransactionDTO();
        dto.setSourceAccountId(1L);
        dto.setTargetAccountId(-1L);
        dto.setCurrency(Currency.EUR);

        var exception = assertThrows(MissingParameterException.class, () -> transactionService.createTransaction(dto));
        assertEquals(ExceptionMessage.MISSING_PARAMETER, exception.getMessage());
    }

    @Test
    void failToCreateTransactionWithInvalidSourceAccountId() {
        TransactionDTO dto = new TransactionDTO();
        dto.setSourceAccountId(-1L);
        dto.setTargetAccountId(2L);
        dto.setAmount(1.0);
        dto.setCurrency(Currency.EUR);

        var exception = assertThrows(AccountNotFoundException.class, () -> transactionService.createTransaction(dto));
        assertEquals(ExceptionMessage.ACCOUNT_NOT_FOUND + " with id: -1", exception.getMessage());
    }

    @Test
    void failToCreateTransactionWithInvalidTargetAccountId() {
        TransactionDTO dto = new TransactionDTO();
        dto.setSourceAccountId(1L);
        dto.setTargetAccountId(-1L);
        dto.setAmount(1.0);
        dto.setCurrency(Currency.EUR);

        var exception = assertThrows(AccountNotFoundException.class, () -> transactionService.createTransaction(dto));
        assertEquals(ExceptionMessage.ACCOUNT_NOT_FOUND + " with id: -1", exception.getMessage());
    }

    @Test
    void failToCreateTransactionWithInvalidAmount() {
        TransactionDTO dto = new TransactionDTO();
        dto.setSourceAccountId(1L);
        dto.setTargetAccountId(2L);
        dto.setAmount(-1.0);
        dto.setCurrency(Currency.EUR);

        var exception = assertThrows(InvalidAmountException.class, () -> transactionService.createTransaction(dto));
        assertEquals(ExceptionMessage.INVALID_AMOUNT, exception.getMessage());
    }

    @Test
    void failToCreateTransactionWithInsufficientBalance() {
        TransactionDTO dto = new TransactionDTO();
        dto.setSourceAccountId(1L);
        dto.setTargetAccountId(2L);
        dto.setAmount(1000.0);
        dto.setCurrency(Currency.EUR);

        var exception = assertThrows(InsufficientBalanceException.class, () -> transactionService.createTransaction(dto));
        assertEquals(ExceptionMessage.INSUFFICIENT_BALANCE, exception.getMessage());
    }

    @Test
    void failToCreateTransactionWithInvalidCurrency() {
        TransactionDTO dto = new TransactionDTO();
        dto.setSourceAccountId(1L);
        dto.setTargetAccountId(2L);
        dto.setAmount(1.0);
        dto.setCurrency(Currency.USD);

        var exception = assertThrows(InvalidCurrencyException.class, () -> transactionService.createTransaction(dto));
        assertEquals(ExceptionMessage.INVALID_CURRENCY, exception.getMessage());
    }

    @Test
    void failToCreateTransactionBetweenSameAccounts() {
        TransactionDTO dto = new TransactionDTO();
        dto.setSourceAccountId(1L);
        dto.setTargetAccountId(1L);
        dto.setAmount(1.0);
        dto.setCurrency(Currency.EUR);

        var exception = assertThrows(SameAccountException.class, () -> transactionService.createTransaction(dto));
        assertEquals(ExceptionMessage.SAME_ACCOUNT, exception.getMessage());
    }
}
