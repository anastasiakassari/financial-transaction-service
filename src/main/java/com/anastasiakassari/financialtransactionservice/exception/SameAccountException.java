package com.anastasiakassari.financialtransactionservice.exception;

public class SameAccountException extends FinancialTransactionServiceException {
    public SameAccountException() {
        super();
    }

    public SameAccountException(String message) {
        super(message);
    }
}
