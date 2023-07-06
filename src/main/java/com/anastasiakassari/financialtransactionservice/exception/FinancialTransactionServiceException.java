package com.anastasiakassari.financialtransactionservice.exception;

public class FinancialTransactionServiceException extends RuntimeException {
    public FinancialTransactionServiceException() {
        super();
    }

    public FinancialTransactionServiceException(String message) {
        super(message);
    }
}
