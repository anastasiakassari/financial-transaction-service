package com.anastasiakassari.financialtransactionservice.exception;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class APIError {

    private String errorMessage;

    private int errorCode;

    private String request;

    private String requestType;

    private String customMessage;

}
