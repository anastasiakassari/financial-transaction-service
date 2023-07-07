package com.anastasiakassari.financialtransactionservice.exception;


public class TransactionNotFoundException extends FinancialTransactionServiceException {
    public TransactionNotFoundException() {
        super();
    }

    public TransactionNotFoundException(String message) {
        super(message);
    }
}
