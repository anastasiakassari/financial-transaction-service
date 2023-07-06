package com.anastasiakassari.financialtransactionservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid currency")
public class InvalidCurrencyException extends FinancialTransactionServiceException {
    public InvalidCurrencyException() {
        super();
    }
    public InvalidCurrencyException(String message) {
        super(message);
    }
}
