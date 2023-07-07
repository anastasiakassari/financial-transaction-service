package com.anastasiakassari.financialtransactionservice.exception;

public class AccountNotFoundException extends FinancialTransactionServiceException {
    public AccountNotFoundException() {
        super();
    }

    public AccountNotFoundException(String message) {
        super(message);
    }
}
