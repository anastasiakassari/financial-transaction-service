package com.anastasiakassari.financialtransactionservice.exception;

public class MissingParameterException extends FinancialTransactionServiceException {
    public MissingParameterException() {
        super();
    }

    public MissingParameterException(String message) {
        super(message);
    }
}
