package com.anastasiakassari.financialtransactionservice.controller;

import com.anastasiakassari.financialtransactionservice.dto.AccountDTO;
import com.anastasiakassari.financialtransactionservice.dto.TransactionDTO;
import com.anastasiakassari.financialtransactionservice.exception.FinancialTransactionServiceException;
import com.anastasiakassari.financialtransactionservice.model.Account;
import com.anastasiakassari.financialtransactionservice.model.Transaction;
import com.anastasiakassari.financialtransactionservice.service.AccountService;
import com.anastasiakassari.financialtransactionservice.service.TransactionService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Schema(description = "Financial Transaction Service")
@OpenAPIDefinition(info = @Info(
        title = "Financial Transaction Service",
        version = "1.0.0",
        description = "API for Financial Transaction Service")
)
@RestController
@RequestMapping("/api/v1/fts")
public class FinancialTransactionController {

    private final AccountService accountService;
    private final TransactionService transactionService;

    /**
     * The application controller, initialized with the specified accountService and transactionService.
     *
     * @param accountService     The account service to be used for account-related operations.
     * @param transactionService The transaction service to be used for transaction-related operations.
     */
    @Autowired
    public FinancialTransactionController(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    /**
     * Retrieves a list of all accounts.
     *
     * @return The list of accounts.
     */
    @GetMapping("/accounts")
    @Operation(tags = {"Financial Transaction Service"}, summary = "Get all accounts")
    public List<Account> getAccounts() {
        return accountService.getAccounts();
    }

    /**
     * Retrieves an account by its ID.
     *
     * @param id The ID of the account.
     * @return The account with the specified ID.
     */
    @GetMapping("/account/{id}")
    @Operation(tags = {"Financial Transaction Service"}, summary = "Get account by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found - The account was not found")
    })
    public Account getAccountById(@PathVariable Long id) {
        return accountService.getAccountById(id);
    }

    /**
     * Creates a new account.
     *
     * @param account The account data to create the account.
     * @return The created account.
     * @throws FinancialTransactionServiceException If an error occurs during the account creation.
     */
    @PostMapping("/account")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(tags = {"Financial Transaction Service"}, summary = "Create new account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created"),
            @ApiResponse(responseCode = "400", description = "Bad request - One or more of the provided parameters is invalid")
    })
    public Account createAccount(@RequestBody AccountDTO account) throws FinancialTransactionServiceException {
        return accountService.createAccount(account);
    }

    /**
     * Retrieves a list of all transactions.
     *
     * @return The list of transactions.
     */
    @GetMapping("/transactions")
    @Operation(tags = {"Financial Transaction Service"}, summary = "Get all transactions")
    public List<Transaction> getTransactions() {
        return transactionService.getTransactions();
    }

    /**
     * Retrieves a transaction by its ID.
     *
     * @param id The ID of the transaction.
     * @return The transaction with the specified ID.
     */
    @GetMapping("/transaction/{id}")
    @Operation(tags = {"Financial Transaction Service"}, summary = "Get transaction by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found - The transaction was not found")
    })
    public Transaction getTransactionById(@PathVariable Long id) {
        return transactionService.getTransactionById(id);
    }

    /**
     * Creates a new transaction.
     *
     * @param transaction The transaction data to create the transaction.
     * @return The created transaction.
     * @throws FinancialTransactionServiceException If an error occurs during the transaction creation.
     */
    @PostMapping("/transaction")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(tags = {"Financial Transaction Service"}, summary = "Create new transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created"),
            @ApiResponse(responseCode = "400", description = "Bad request - One or more of the provided parameters is invalid"),
            @ApiResponse(responseCode = "404", description = "Not found - One or more of the provided accounts does not exist")
    })
    public Transaction createTransaction(@RequestBody TransactionDTO transaction) throws FinancialTransactionServiceException {
        return transactionService.createTransaction(transaction);
    }

}

