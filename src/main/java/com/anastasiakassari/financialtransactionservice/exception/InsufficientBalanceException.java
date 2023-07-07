package com.anastasiakassari.financialtransactionservice.exception;

public class InsufficientBalanceException extends FinancialTransactionServiceException {
    public InsufficientBalanceException() {
        super();
    }

    public InsufficientBalanceException(String message) {
        super(message);
    }
}
