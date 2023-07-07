package com.anastasiakassari.financialtransactionservice.exception;

import com.anastasiakassari.financialtransactionservice.controller.FinancialTransactionController;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

@SpringBootTest
@ActiveProfiles("test")
class GlobalExceptionHandlerTest {

    private final HttpServletRequest mockRequest = mock(HttpServletRequest.class);

    @Test
    void accountNotFoundExceptionHandler() {
        AccountNotFoundException exception = new AccountNotFoundException("Account not found");
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ResponseEntity<APIError> response = handler.accountNotFoundException(exception, mockRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ExceptionMessage.ACCOUNT_NOT_FOUND, response.getBody().getErrorMessage());
    }

    @Test
    void financialTransactionServiceExceptionHandler() {
        FinancialTransactionServiceException exception = new FinancialTransactionServiceException("Invalid request");
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ResponseEntity<APIError> response = handler.financialTransactionServiceException(exception, mockRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ExceptionMessage.INVALID_REQUEST, response.getBody().getErrorMessage());
    }

    @Test
    void insufficientBalanceExceptionHandler() {
        InsufficientBalanceException exception = new InsufficientBalanceException("Insufficient balance");
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ResponseEntity<APIError> response = handler.insufficientBalanceException(exception, mockRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ExceptionMessage.INSUFFICIENT_BALANCE, response.getBody().getErrorMessage());

    }

    @Test
    void invalidAmountExceptionHandler() {
        InvalidAmountException exception = new InvalidAmountException("Invalid amount");
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ResponseEntity<APIError> response = handler.invalidAmountException(exception, mockRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ExceptionMessage.INVALID_AMOUNT, response.getBody().getErrorMessage());
    }

    @Test
    void invalidParametersExceptionHandler() {
        InvalidParametersException exception = new InvalidParametersException("Invalid parameters");
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ResponseEntity<APIError> response = handler.invalidParametersException(exception, mockRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ExceptionMessage.INVALID_PARAMETERS, response.getBody().getErrorMessage());
    }

    @Test
    void missingParameterExceptionHandler() {
        MissingParameterException exception = new MissingParameterException("Missing parameters");
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ResponseEntity<APIError> response = handler.missingParameterException(exception, mockRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ExceptionMessage.MISSING_PARAMETER, response.getBody().getErrorMessage());
    }

    @Test
    void sameAccountException() {
        SameAccountException exception = new SameAccountException("Same account");
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ResponseEntity<APIError> response = handler.sameAccountException(exception, mockRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ExceptionMessage.SAME_ACCOUNT, response.getBody().getErrorMessage());
    }

    @Test
    void transactionNotFoundException() {
        TransactionNotFoundException exception = new TransactionNotFoundException("Transaction not found");
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ResponseEntity<APIError> response = handler.transactionNotFoundException(exception, mockRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ExceptionMessage.TRANSACTION_NOT_FOUND, response.getBody().getErrorMessage());
    }

    @Test
    void genericExceptionHandler() {
        Exception exception = new Exception("Internal server error");
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ResponseEntity<APIError> response = handler.genericException(exception, mockRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Internal server error", response.getBody().getErrorMessage());
    }

    @Test
    void testControllerAdvice() throws Exception {
        FinancialTransactionController mockController = Mockito.mock(FinancialTransactionController.class);
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(mockController)
                .setControllerAdvice(handler)
                .build();

        Mockito.when(mockController.getAccountById(Mockito.anyLong())).thenThrow(new AccountNotFoundException("Account not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/fts/account/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
