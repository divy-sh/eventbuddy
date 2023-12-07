package com.eventbuddy.eventbuddy.dao;

import com.eventbuddy.eventbuddy.model.Ticket;
import com.eventbuddy.eventbuddy.model.Transaction;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionDao {

  @Autowired
  private QueryManager queryManager;

  public Transaction createTransaction(Transaction transaction) {
    String query = "call insert_user_transaction(?, ?, ?, ?, ?)";
    List<Transaction> result = queryManager.runQuery(query, Transaction.class, new Date(),
        transaction.getStatus(), transaction.getCardNum(),
        transaction.getEmailId(), transaction.getEventId());
    if (result.isEmpty()) {
      return null;
    }
    return result.get(0);
  }

  public Transaction updateTransaction(Transaction transaction) {
    String query = "call update_user_transaction(?, ?, ?, ?, ?, ?)";
    List<Transaction> result = queryManager.runQuery(query, Transaction.class,
        transaction.getTransactionId(), new Date(), transaction.getStatus(),
        transaction.getCardNum(), transaction.getEmailId(), transaction.getEventId());
    if (result.isEmpty()) {
      return null;
    }
    return result.get(0);
  }

  public Ticket generateTicket(Transaction transaction) {
    String query = "call insert_event_ticket()";
    return null;
  }
}
