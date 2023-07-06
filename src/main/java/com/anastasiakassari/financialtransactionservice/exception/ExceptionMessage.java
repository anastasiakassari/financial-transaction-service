package com.anastasiakassari.financialtransactionservice.exception;

public class ExceptionMessage {

    public static final String ACCOUNT_NOT_FOUND = "Account not found";
    public static final String INSUFFICIENT_BALANCE = "Insufficient balance";
    public static final String INVALID_AMOUNT = "Invalid amount";
    public static final String INVALID_CURRENCY = "Invalid currency";
    public static final String INVALID_PARAMETERS = "Invalid parameters";
    public static final String MISSING_PARAMETER = "One or more parameters are missing";
    public static final String SAME_ACCOUNT = "Source and target accounts cannot be the same";
    public static final String TRANSACTION_NOT_FOUND = "Transaction not found";

    private ExceptionMessage() {
        throw new IllegalStateException("Utility class");
    }

}
