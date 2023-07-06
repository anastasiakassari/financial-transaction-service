package com.anastasiakassari.financialtransactionservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = ExceptionMessage.INVALID_PARAMETERS)
public class InvalidParametersException extends FinancialTransactionServiceException {
    public InvalidParametersException() {
        super();
    }

    public InvalidParametersException(String message) {
        super(message);
    }
}
