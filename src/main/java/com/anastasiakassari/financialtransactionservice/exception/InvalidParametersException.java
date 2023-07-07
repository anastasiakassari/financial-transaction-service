package com.anastasiakassari.financialtransactionservice.exception;

public class InvalidParametersException extends FinancialTransactionServiceException {
    public InvalidParametersException() {
        super();
    }

    public InvalidParametersException(String message) {
        super(message);
    }
}
