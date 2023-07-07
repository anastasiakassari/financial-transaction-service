package com.anastasiakassari.financialtransactionservice.exception;

public class InvalidCurrencyException extends FinancialTransactionServiceException {
    public InvalidCurrencyException() {
        super();
    }

    public InvalidCurrencyException(String message) {
        super(message);
    }
}
