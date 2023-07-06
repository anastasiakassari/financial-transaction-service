package com.anastasiakassari.financialtransactionservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = ExceptionMessage.SAME_ACCOUNT)
public class SameAccountException extends FinancialTransactionServiceException {
    public SameAccountException() {
        super();
    }

    public SameAccountException(String message) {
        super(message);
    }
}
