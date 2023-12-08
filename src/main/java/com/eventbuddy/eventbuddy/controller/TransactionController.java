package com.eventbuddy.eventbuddy.controller;

import com.eventbuddy.eventbuddy.Utils.BuddyError;
import com.eventbuddy.eventbuddy.Utils.ErrorResponse;
import com.eventbuddy.eventbuddy.model.Transaction;
import com.eventbuddy.eventbuddy.service.TransactionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "transaction")
public class TransactionController {

  @Autowired
  private TransactionService transactionService;

  @PostMapping(value = "buy/ticket", produces = "application/json")
  public ResponseEntity<?> buyTicket(@RequestBody Transaction transaction) {
    try {
      Transaction result = transactionService.buyTicket(transaction);
      return ResponseEntity.ok(result);
    } catch (IllegalArgumentException | BuddyError e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e));
    }
  }

  @GetMapping(value = "get", produces = "application/json")
  public ResponseEntity<?> getUserTransactions(@RequestParam("email_id") String emailId) {
    try {
      List<Transaction> result = transactionService.getUserTransactions(emailId);
      return ResponseEntity.ok(result);
    } catch (IllegalArgumentException | BuddyError e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e));
    }
  }
}
