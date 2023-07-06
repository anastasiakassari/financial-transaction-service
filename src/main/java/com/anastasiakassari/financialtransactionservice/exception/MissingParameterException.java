package com.anastasiakassari.financialtransactionservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = ExceptionMessage.MISSING_PARAMETER)
public class MissingParameterException extends FinancialTransactionServiceException {
    public MissingParameterException() {
        super();
    }

    public MissingParameterException(String message) {
        super(message);
    }
}
