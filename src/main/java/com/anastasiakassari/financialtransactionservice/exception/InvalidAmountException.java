package com.anastasiakassari.financialtransactionservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = ExceptionMessage.INVALID_AMOUNT)
public class InvalidAmountException extends FinancialTransactionServiceException {
    public InvalidAmountException() {
        super();
    }

    public InvalidAmountException(String message) {
        super(message);
    }
}
