package com.anastasiakassari.financialtransactionservice.service;

import com.anastasiakassari.financialtransactionservice.dto.AccountDTO;
import com.anastasiakassari.financialtransactionservice.exception.*;
import com.anastasiakassari.financialtransactionservice.model.Account;
import com.anastasiakassari.financialtransactionservice.model.Currency;
import com.anastasiakassari.financialtransactionservice.repository.AccountRepository;
import com.anastasiakassari.financialtransactionservice.repository.TransactionRepository;
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
class AccountServiceTest {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    private AccountService accountService;

    @Test
    void testGetAccounts() {
        List<Account> accounts = accountService.getAccounts();
        assertNotNull(accounts);
        int initialSize = accounts.size();

        Account account = accountRepository.save(new Account(null, 1.0, Currency.USD, new Timestamp(System.currentTimeMillis())));

        accounts = accountService.getAccounts();
        Optional<Account> optional = accounts.stream().filter(a -> a.getId().equals(account.getId())).findFirst();
        assertTrue(optional.isPresent());
        Account serviceAccount = optional.get();
        assertEquals(account, serviceAccount);
        assertEquals(initialSize + 1, accounts.size());
    }

    @Test
    void testGetAccountById() {
        Account account = accountRepository.save(new Account(null, 100.0, Currency.GBP, new Timestamp(System.currentTimeMillis())));
        Account serviceAccount = accountService.getAccountById(account.getId());
        assertNotNull(serviceAccount);
        assertEquals(account, serviceAccount);
    }

    @Test
    void testGetAccountFailsWithInvalidId() {
        long invalidId = -1L;
        var exception = assertThrows(AccountNotFoundException.class, () -> accountService.getAccountById(invalidId));
        assertEquals(ExceptionMessage.ACCOUNT_NOT_FOUND + " with ID: " + invalidId, exception.getMessage());
    }

    @Test
    void testCreateAccountWithAllParams() {
        AccountDTO dto = new AccountDTO();
        dto.setBalance(10.0);
        dto.setCurrency(Currency.USD);

        Account account = accountService.createAccount(dto);
        assertNotNull(account);
        assertEquals(10.0, account.getBalance());
        assertEquals(Currency.USD, account.getCurrency());
    }

    @Test
    void testCreateAccountWithoutBalance() {
        AccountDTO dto = new AccountDTO();
        dto.setCurrency(Currency.USD);

        Account account = accountService.createAccount(dto);
        assertNotNull(account);
        assertEquals(0.0, account.getBalance());
        assertEquals(Currency.USD, account.getCurrency());
    }

    @Test
    void testCreateAccountFailsWithoutParams() {
        AccountDTO dto = new AccountDTO();
        var exception = assertThrows(MissingParameterException.class, () -> accountService.createAccount(dto));
        assertEquals("One or more parameters are missing", exception.getMessage());
    }

    @Test
    void testCreateAccountFailsWithInvalidBalance() {
        AccountDTO dto = new AccountDTO();
        dto.setCurrency(Currency.USD);
        dto.setBalance(-10.0);
        var exception = assertThrows(InvalidAmountException.class, () -> accountService.createAccount(dto));
        assertEquals(ExceptionMessage.INVALID_AMOUNT, exception.getMessage());
    }

    @Test
    void testUpdateAccountWithValidAccount() {
        Account account = accountRepository.save(new Account(null, 100.0, Currency.USD, new Timestamp(System.currentTimeMillis())));
        Account updatedAccount = new Account(account.getId(), 200.0, Currency.EUR, account.getCreatedAt());
        Account result = accountService.updateAccount(updatedAccount);
        assertEquals(updatedAccount, result);
    }

    @Test
    void testUpdateAccountFailsWithInvalidAccountParams() {
        Account account = new Account(null, 200.0, Currency.EUR, new Timestamp(System.currentTimeMillis()));

        var exception = assertThrows(InvalidParametersException.class, () -> accountService.updateAccount(account));
        assertEquals(ExceptionMessage.INVALID_PARAMETERS, exception.getMessage());
    }

    @Test
    void testUpdateAccountFailsWithInvalidAccountId() {
        long invalidId = 123456L;
        Account account = new Account(invalidId, 200.0, Currency.EUR, new Timestamp(System.currentTimeMillis()));

        var exception = assertThrows(AccountNotFoundException.class, () -> accountService.updateAccount(account));
        assertEquals(ExceptionMessage.ACCOUNT_NOT_FOUND + " with ID: " + invalidId, exception.getMessage());
    }

    @Test
    void testUpdateAccountFailsWithInvalidBalance() {
        Account account = accountRepository.save(new Account(null, 100.0, Currency.USD, new Timestamp(System.currentTimeMillis())));
        Account updatedAccount = new Account(account.getId(), -100.0, Currency.USD, account.getCreatedAt());

        var exception = assertThrows(InvalidAmountException.class, () -> accountService.updateAccount(updatedAccount));
        assertEquals(ExceptionMessage.INVALID_AMOUNT, exception.getMessage());
    }

    @Test
    void testDeleteAccountById() {
        Account account = accountRepository.save(new Account(null, 100.0, Currency.USD, new Timestamp(System.currentTimeMillis())));
        long id = account.getId();
        assertTrue(accountService.deleteAccount(id));
        assertTrue(accountRepository.findById(id).isEmpty());
    }

    @Test
    void testDeleteAccountFailsWithInvalidAccountId() {
        long invalidId = 123456L;
        var exception = assertThrows(AccountNotFoundException.class, () -> accountService.deleteAccount(invalidId));
        assertEquals(ExceptionMessage.ACCOUNT_NOT_FOUND + " with ID: " + invalidId, exception.getMessage());
    }
}