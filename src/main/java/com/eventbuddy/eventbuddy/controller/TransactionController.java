package com.eventbuddy.eventbuddy.controller;

import com.eventbuddy.eventbuddy.Utils.BuddyError;
import com.eventbuddy.eventbuddy.Utils.ErrorResponse;
import com.eventbuddy.eventbuddy.model.Ticket;
import com.eventbuddy.eventbuddy.model.Transaction;
import com.eventbuddy.eventbuddy.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping(value = "transaction")
public class TransactionController {

  @Autowired
  private TransactionService transactionService;

  @PostMapping(value = "buy/ticket", produces = "application/json")
  public ResponseEntity<?> buyTicket(@RequestBody Transaction transaction) {
    try {
      Ticket result = transactionService.buyTicket(transaction);
      return ResponseEntity.ok(result);
    } catch (IllegalArgumentException | BuddyError e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e));
    }
  }
}
