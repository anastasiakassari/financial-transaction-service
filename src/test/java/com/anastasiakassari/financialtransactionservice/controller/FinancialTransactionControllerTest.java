package com.anastasiakassari.financialtransactionservice.controller;

import com.anastasiakassari.financialtransactionservice.FinancialTransactionServiceApplication;
import com.anastasiakassari.financialtransactionservice.dto.AccountDTO;
import com.anastasiakassari.financialtransactionservice.dto.TransactionDTO;
import com.anastasiakassari.financialtransactionservice.exception.*;
import com.anastasiakassari.financialtransactionservice.model.Account;
import com.anastasiakassari.financialtransactionservice.model.Currency;
import com.anastasiakassari.financialtransactionservice.model.Transaction;
import com.anastasiakassari.financialtransactionservice.service.AccountService;
import com.anastasiakassari.financialtransactionservice.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@WebMvcTest(FinancialTransactionController.class)
@ContextConfiguration(classes = FinancialTransactionServiceApplication.class)
@ActiveProfiles("test")
class FinancialTransactionControllerTest {

    public static final String URL_API = "/api/v1/fts";

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AccountService accountService;

    @MockBean
    private TransactionService transactionService;

    private List<Account> accounts;
    private List<Transaction> transactions;

    @BeforeEach
    void setUp() {
        accounts = new ArrayList<>();
        accounts.add(new Account(1L, 250.0, Currency.EUR, new Timestamp(System.currentTimeMillis())));
        accounts.add(new Account(2L, 100.0, Currency.EUR, new Timestamp(System.currentTimeMillis())));
        accounts.add(new Account(3L, 0.0, Currency.USD, new Timestamp(System.currentTimeMillis())));
        transactions = new ArrayList<>();
        transactions.add(new Transaction(1L, 1L, 2L, 100.0, Currency.EUR));
        transactions.add(new Transaction(2L, 2L, 1L, 50.0, Currency.EUR));
    }

    @Test
    void shouldGetAccounts() throws Exception {
        Account account = accounts.get(0);
        when(accountService.getAccounts()).thenReturn(accounts);

        mockMvc.perform(MockMvcRequestBuilders.get(URL_API + "/accounts"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(accounts.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(account.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].currency").value(account.getCurrency().name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].balance").value(account.getBalance()));

        verify(accountService, times(1)).getAccounts();

    }

    @Test
    void shouldGetAccountsReturnsEmptyList() throws Exception {
        when(accountService.getAccounts()).thenReturn(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders.get(URL_API + "/accounts"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(0));

        verify(accountService, times(1)).getAccounts();

    }

    @Test
    void shouldGetAccountById() throws Exception {
        when(accountService.getAccountById(1L)).thenReturn(accounts.get(0));

        mockMvc.perform(MockMvcRequestBuilders.get(URL_API + "/account/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L));

        verify(accountService, times(1)).getAccountById(1L);
    }

    @Test
    void shouldFailToGetAccountById() throws Exception {
        long invalidId = 123L;
        when(accountService.getAccountById(invalidId)).thenThrow(AccountNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get(URL_API + "/account/" + invalidId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(accountService, times(1)).getAccountById(invalidId);
    }

    @Test
    void shouldCreateAccountWithAllParams() throws Exception {
        Account account = accounts.get(0);
        AccountDTO dto = new AccountDTO();
        dto.setCurrency(account.getCurrency());
        dto.setBalance(account.getBalance());
        when(accountService.createAccount(any(AccountDTO.class))).thenReturn(account);

        mockMvc.perform(MockMvcRequestBuilders.post(URL_API + "/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(account.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currency").value(account.getCurrency().name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(account.getBalance()));

    }

    @Test
    void shouldCreateAccountWithoutBalance() throws Exception {
        Account account = accounts.get(2);
        AccountDTO dto = new AccountDTO();
        dto.setCurrency(account.getCurrency());
        when(accountService.createAccount(any(AccountDTO.class))).thenReturn(account);

        mockMvc.perform(MockMvcRequestBuilders.post(URL_API + "/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(account.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currency").value(account.getCurrency().name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(account.getBalance()));

    }

    @Test
    void shouldFailToCreateAccountWithoutParams() throws Exception {
        AccountDTO dto = new AccountDTO();
        when(accountService.createAccount(any(AccountDTO.class))).thenThrow(MissingParameterException.class);

        mockMvc.perform(MockMvcRequestBuilders.post(URL_API + "/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    void shouldFailToCreateAccountWithInvalidBalance() throws Exception {
        AccountDTO dto = new AccountDTO();
        dto.setCurrency(Currency.USD);
        dto.setBalance(-10.0);
        when(accountService.createAccount(any(AccountDTO.class))).thenThrow(InvalidAmountException.class);

        mockMvc.perform(MockMvcRequestBuilders.post(URL_API + "/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    void shouldGetTransactions() throws Exception {
        Transaction transaction = transactions.get(0);
        when(transactionService.getTransactions()).thenReturn(transactions);

        mockMvc.perform(MockMvcRequestBuilders.get(URL_API + "/transactions"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(transactions.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(transaction.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].sourceAccountId").value(transaction.getSourceAccountId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].targetAccountId").value(transaction.getTargetAccountId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].currency").value(transaction.getCurrency().name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].amount").value(transaction.getAmount()));

        verify(transactionService, times(1)).getTransactions();

    }

    @Test
    void shouldGetTransactionsReturnsEmptyList() throws Exception {
        when(transactionService.getTransactions()).thenReturn(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders.get(URL_API + "/transactions"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(0));

        verify(transactionService, times(1)).getTransactions();

    }

    @Test
    void shouldGetTransactionById() throws Exception {
        when(transactionService.getTransactionById(1L)).thenReturn(transactions.get(0));

        mockMvc.perform(MockMvcRequestBuilders.get(URL_API + "/transaction/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L));

        verify(transactionService, times(1)).getTransactionById(1L);
    }

    @Test
    void shouldFailToGetTransactionById() throws Exception {
        when(transactionService.getTransactionById(123L)).thenThrow(TransactionNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get(URL_API + "/transaction/123"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(transactionService, times(1)).getTransactionById(123L);
    }

    @Test
    void shouldCreateTransactionWithAllParams() throws Exception {
        Transaction transaction = transactions.get(0);
        TransactionDTO dto = new TransactionDTO();
        dto.setSourceAccountId(transaction.getSourceAccountId());
        dto.setTargetAccountId(transaction.getTargetAccountId());
        dto.setAmount(transaction.getAmount());
        dto.setCurrency(transaction.getCurrency());
        when(transactionService.createTransaction(any(TransactionDTO.class))).thenReturn(transaction);
        when(accountService.getAccountById(dto.getSourceAccountId())).thenReturn(accounts.get(0));
        when(accountService.getAccountById(dto.getTargetAccountId())).thenReturn(accounts.get(1));

        mockMvc.perform(MockMvcRequestBuilders.post(URL_API + "/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(transaction.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sourceAccountId").value(transaction.getSourceAccountId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.targetAccountId").value(transaction.getTargetAccountId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currency").value(transaction.getCurrency().name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(transaction.getAmount()));

    }

    @Test
    void shouldFailToCreateTransactionWithoutParams() throws Exception {
        TransactionDTO dto = new TransactionDTO();
        when(transactionService.createTransaction(any(TransactionDTO.class))).thenThrow(MissingParameterException.class);

        mockMvc.perform(MockMvcRequestBuilders.post(URL_API + "/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    void shouldFailToCreateTransactionWithInvalidSourceAccountId() throws Exception {
        TransactionDTO dto = new TransactionDTO();
        dto.setSourceAccountId(123L);
        dto.setTargetAccountId(1L);
        dto.setAmount(10.0);
        dto.setCurrency(Currency.USD);
        when(transactionService.createTransaction(any(TransactionDTO.class))).thenThrow(AccountNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.post(URL_API + "/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldFailToCreateTransactionWithInvalidTargetAccountId() throws Exception {
        TransactionDTO dto = new TransactionDTO();
        dto.setSourceAccountId(1L);
        dto.setTargetAccountId(123L);
        dto.setAmount(10.0);
        dto.setCurrency(Currency.USD);
        when(transactionService.createTransaction(any(TransactionDTO.class))).thenThrow(AccountNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.post(URL_API + "/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldFailToCreateTransactionBetweenSameAccounts() throws Exception {
        TransactionDTO dto = new TransactionDTO();
        dto.setSourceAccountId(1L);
        dto.setTargetAccountId(1L);
        dto.setAmount(10.0);
        dto.setCurrency(Currency.USD);
        when(transactionService.createTransaction(any(TransactionDTO.class))).thenThrow(SameAccountException.class);

        mockMvc.perform(MockMvcRequestBuilders.post(URL_API + "/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @Test
    void shouldFailToCreateTransactionWithInvalidAmount() throws Exception {
        TransactionDTO dto = new TransactionDTO();
        dto.setSourceAccountId(1L);
        dto.setTargetAccountId(1L);
        dto.setAmount(-10.0);
        dto.setCurrency(Currency.USD);
        when(transactionService.createTransaction(any(TransactionDTO.class))).thenThrow(InvalidAmountException.class);

        mockMvc.perform(MockMvcRequestBuilders.post(URL_API + "/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @Test
    void shouldFailToCreateTransactionWithInvalidCurrency() throws Exception {
        TransactionDTO dto = new TransactionDTO();
        dto.setSourceAccountId(1L);
        dto.setTargetAccountId(3L);
        dto.setAmount(10.0);
        dto.setCurrency(Currency.EUR);
        when(transactionService.createTransaction(any(TransactionDTO.class))).thenThrow(InvalidCurrencyException.class);

        mockMvc.perform(MockMvcRequestBuilders.post(URL_API + "/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    void shouldFailToCreateTransactionWithInsufficientBalance() throws Exception {
        TransactionDTO dto = new TransactionDTO();
        dto.setSourceAccountId(1L);
        dto.setTargetAccountId(3L);
        dto.setAmount(1000.0);
        dto.setCurrency(Currency.USD);
        when(transactionService.createTransaction(any(TransactionDTO.class))).thenThrow(InsufficientBalanceException.class);

        mockMvc.perform(MockMvcRequestBuilders.post(URL_API + "/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }
}
