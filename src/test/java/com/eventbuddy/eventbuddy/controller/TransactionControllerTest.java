package com.eventbuddy.eventbuddy.controller;

import com.eventbuddy.eventbuddy.Utils.BuddyError;
import com.eventbuddy.eventbuddy.Utils.ErrorResponse;
import com.eventbuddy.eventbuddy.model.Transaction;
import com.eventbuddy.eventbuddy.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionControllerTest {

    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private TransactionService transactionService;

    private Transaction sampleTransaction;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleTransaction = new Transaction();
        sampleTransaction.setTransactionId(123);
        sampleTransaction.setEmailId("user@example.com");
        // Add other necessary fields here
    }

    @Test
    void testBuyTicketSuccess() throws BuddyError {
        when(transactionService.buyTicket(any(Transaction.class))).thenReturn(sampleTransaction);

        ResponseEntity<?> response = transactionController.buyTicket(sampleTransaction);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleTransaction, response.getBody());
    }

    @Test
    void testBuyTicketFailure() throws BuddyError {
        when(transactionService.buyTicket(any(Transaction.class)))
                .thenThrow(new BuddyError("Transaction failed"));

        ResponseEntity<?> response = transactionController.buyTicket(sampleTransaction);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Transaction failed", errorResponse.getError());
    }

    @Test
    void testGetUserTransactionsSuccess() throws BuddyError {
        List<Transaction> transactions = Arrays.asList(sampleTransaction);
        when(transactionService.getUserTransactions("user@example.com")).thenReturn(transactions);

        ResponseEntity<?> response = transactionController.getUserTransactions("user@example.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactions, response.getBody());
    }

    @Test
    void testGetUserTransactionsFailure() throws BuddyError {
        when(transactionService.getUserTransactions("user@example.com"))
                .thenThrow(new BuddyError("No transactions found"));

        ResponseEntity<?> response = transactionController.getUserTransactions("user@example.com");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertNotNull(errorResponse);
        assertEquals("No transactions found", errorResponse.getError());
    }
}
