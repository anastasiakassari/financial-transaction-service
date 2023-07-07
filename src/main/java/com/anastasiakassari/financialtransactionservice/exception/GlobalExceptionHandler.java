package com.anastasiakassari.financialtransactionservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final String REQUEST_NOT_VALID = "Request is not valid";

    @ExceptionHandler({AccountNotFoundException.class})
    public ResponseEntity<APIError> accountNotFoundException(AccountNotFoundException accountNotFoundException, HttpServletRequest request) {
        String errorMessage = "AccountNotFoundException: " + accountNotFoundException.getMessage() + " for " + request.getRequestURI();
        logger.error(errorMessage);
        return new ResponseEntity<>(
                APIError.builder()
                        .errorMessage(ExceptionMessage.ACCOUNT_NOT_FOUND)
                        .errorCode(HttpStatus.NOT_FOUND.value())
                        .request(request.getRequestURI())
                        .requestType(request.getMethod())
                        .customMessage(REQUEST_NOT_VALID)
                        .build(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(FinancialTransactionServiceException.class)
    public ResponseEntity<APIError> financialTransactionServiceException(FinancialTransactionServiceException financialTransactionServiceException, HttpServletRequest request) {
        String errorMessage = "FinancialTransactionServiceException: " + financialTransactionServiceException.getMessage() + " for " + request.getRequestURI();
        logger.error(errorMessage);
        return new ResponseEntity<>(
                APIError.builder()
                        .errorMessage(ExceptionMessage.INVALID_REQUEST)
                        .errorCode(HttpStatus.BAD_REQUEST.value())
                        .request(request.getRequestURI())
                        .requestType(request.getMethod())
                        .customMessage(REQUEST_NOT_VALID)
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({InsufficientBalanceException.class})
    public ResponseEntity<APIError> insufficientBalanceException(InsufficientBalanceException insufficientBalanceException, HttpServletRequest request) {
        String errorMessage = "InsufficientBalanceException: " + insufficientBalanceException.getMessage() + " for " + request.getRequestURI();
        logger.error(errorMessage);
        return new ResponseEntity<>(
                APIError.builder()
                        .errorMessage(ExceptionMessage.INSUFFICIENT_BALANCE)
                        .errorCode(HttpStatus.BAD_REQUEST.value())
                        .request(request.getRequestURI())
                        .requestType(request.getMethod())
                        .customMessage(REQUEST_NOT_VALID)
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({InvalidAmountException.class})
    public ResponseEntity<APIError> invalidAmountException(InvalidAmountException invalidAmountException, HttpServletRequest request) {
        String errorMessage = "InvalidAmountException: " + invalidAmountException.getMessage() + " for " + request.getRequestURI();
        logger.error(errorMessage);
        return new ResponseEntity<>(
                APIError.builder()
                        .errorMessage(ExceptionMessage.INVALID_AMOUNT)
                        .errorCode(HttpStatus.BAD_REQUEST.value())
                        .request(request.getRequestURI())
                        .requestType(request.getMethod())
                        .customMessage(REQUEST_NOT_VALID)
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({InvalidCurrencyException.class})
    public ResponseEntity<APIError> invalidCurrencyException(InvalidCurrencyException invalidCurrencyException, HttpServletRequest request) {
        String errorMessage = "InvalidCurrencyException: " + invalidCurrencyException.getMessage() + " for " + request.getRequestURI();
        logger.error(errorMessage);
        return new ResponseEntity<>(
                APIError.builder()
                        .errorMessage(ExceptionMessage.INVALID_CURRENCY)
                        .errorCode(HttpStatus.BAD_REQUEST.value())
                        .request(request.getRequestURI())
                        .requestType(request.getMethod())
                        .customMessage(REQUEST_NOT_VALID)
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({InvalidParametersException.class})
    public ResponseEntity<APIError> invalidParametersException(InvalidParametersException invalidParametersException, HttpServletRequest request) {
        String errorMessage = "InvalidParametersException: " + invalidParametersException.getMessage() + " for " + request.getRequestURI();
        logger.error(errorMessage);
        return new ResponseEntity<>(
                APIError.builder()
                        .errorMessage(ExceptionMessage.INVALID_PARAMETERS)
                        .errorCode(HttpStatus.BAD_REQUEST.value())
                        .request(request.getRequestURI())
                        .requestType(request.getMethod())
                        .customMessage(REQUEST_NOT_VALID)
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({MissingParameterException.class})
    public ResponseEntity<APIError> missingParameterException(MissingParameterException missingParameterException, HttpServletRequest request) {
        String errorMessage = "MissingParameterException: " + missingParameterException.getMessage() + " for " + request.getRequestURI();
        logger.error(errorMessage);
        return new ResponseEntity<>(
                APIError.builder()
                        .errorMessage(ExceptionMessage.MISSING_PARAMETER)
                        .errorCode(HttpStatus.BAD_REQUEST.value())
                        .request(request.getRequestURI())
                        .requestType(request.getMethod())
                        .customMessage(REQUEST_NOT_VALID)
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({SameAccountException.class})
    public ResponseEntity<APIError> sameAccountException(SameAccountException sameAccountException, HttpServletRequest request) {
        String errorMessage = "SameAccountException: " + sameAccountException.getMessage() + " for " + request.getRequestURI();
        logger.error(errorMessage);
        return new ResponseEntity<>(
                APIError.builder()
                        .errorMessage(ExceptionMessage.SAME_ACCOUNT)
                        .errorCode(HttpStatus.BAD_REQUEST.value())
                        .request(request.getRequestURI())
                        .requestType(request.getMethod())
                        .customMessage(REQUEST_NOT_VALID)
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({TransactionNotFoundException.class})
    public ResponseEntity<APIError> transactionNotFoundException(TransactionNotFoundException transactionNotFoundException, HttpServletRequest request) {
        String errorMessage = "TransactionNotFoundException: " + transactionNotFoundException.getMessage() + " for " + request.getRequestURI();
        logger.error(errorMessage);
        return new ResponseEntity<>(
                APIError.builder()
                        .errorMessage(ExceptionMessage.TRANSACTION_NOT_FOUND)
                        .errorCode(HttpStatus.NOT_FOUND.value())
                        .request(request.getRequestURI())
                        .requestType(request.getMethod())
                        .customMessage(REQUEST_NOT_VALID)
                        .build(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<APIError> genericException(Exception exception, HttpServletRequest request) {
        String errorMessage = "Exception : " + exception.getLocalizedMessage() + " for " + request.getRequestURI();
        logger.error(errorMessage);

        return new ResponseEntity<>(
                APIError.builder()
                        .errorMessage(exception.getLocalizedMessage())
                        .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .request(request.getRequestURI())
                        .requestType(request.getMethod())
                        .customMessage("Could not process request")
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

}