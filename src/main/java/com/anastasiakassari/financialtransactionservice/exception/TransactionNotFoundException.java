package com.anastasiakassari.financialtransactionservice.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = ExceptionMessage.TRANSACTION_NOT_FOUND)
public class TransactionNotFoundException extends FinancialTransactionServiceException {
    public TransactionNotFoundException() {
        super();
    }

    public TransactionNotFoundException(String message) {
        super(message);
    }
}
