package com.anastasiakassari.financialtransactionservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = ExceptionMessage.ACCOUNT_NOT_FOUND)
public class AccountNotFoundException extends FinancialTransactionServiceException {
    public AccountNotFoundException() {
        super();
    }

    public AccountNotFoundException(String message) {
        super(message);
    }
}
