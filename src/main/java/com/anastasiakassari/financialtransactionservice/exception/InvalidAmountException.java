package com.anastasiakassari.financialtransactionservice.exception;

public class InvalidAmountException extends FinancialTransactionServiceException {
    public InvalidAmountException() {
        super();
    }

    public InvalidAmountException(String message) {
        super(message);
    }
}
