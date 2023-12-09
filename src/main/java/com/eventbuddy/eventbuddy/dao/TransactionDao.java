package com.eventbuddy.eventbuddy.dao;

import com.eventbuddy.eventbuddy.Utils.BuddyError;
import com.eventbuddy.eventbuddy.model.Transaction;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionDao {

  @Autowired
  private QueryManager queryManager;

  public List<Transaction> getUserTransactions(String emailId) throws BuddyError {
    String query = "call get_user_transaction(?)";
    return queryManager.runQuery(query, Transaction.class, emailId);
  }

  public Transaction createTransaction(Transaction transaction) throws BuddyError {
    String query = "call initiate_user_transaction(?, ?, ?, ?)";
    List<Transaction> result = queryManager.runQuery(query, Transaction.class, new Date(),
        transaction.getEmailId(), transaction.getEventId(),
        transaction.getTotalTickets());
    if (result.isEmpty()) {
      return null;
    }
    return result.getFirst();
  }

  public Transaction finalizeTransaction(Transaction result) throws BuddyError {
    String query = "call successful_user_transaction(?, ?, ?)";
    List<Transaction> fin = queryManager.runQuery(query, Transaction.class, new Date(),
        result.getTransactionId(), result.getCardNum());
    if (fin.isEmpty()) {
      return null;
    }
    return fin.getFirst();
  }

  public void failTransaction(Transaction result) throws BuddyError {
    String query = "call failed_user_transaction(?, ?)";
    queryManager.update(query, new Date(), result.getTransactionId());
  }
}
