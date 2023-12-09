package com.eventbuddy.eventbuddy.service;

import com.eventbuddy.eventbuddy.Utils.BuddyError;
import com.eventbuddy.eventbuddy.dao.EventDao;
import com.eventbuddy.eventbuddy.dao.TransactionDao;
import com.eventbuddy.eventbuddy.dao.UserDao;
import com.eventbuddy.eventbuddy.model.Card;
import com.eventbuddy.eventbuddy.model.Event;
import com.eventbuddy.eventbuddy.model.Transaction;
import com.eventbuddy.eventbuddy.model.User;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

  @Autowired
  private TransactionDao transactionDao;
  @Autowired
  private EventDao eventDao;
  @Autowired
  private UserDao userDao;

  public Transaction buyTicket(Transaction transaction) throws BuddyError {
    Event event = eventDao.getEvent(transaction.getEventId());
    if (event == null) {
      throw new BuddyError("invalid event id");
    }
    User user = userDao.getUserDetail(transaction.getEmailId());
    if (user == null) {
      throw new BuddyError("invalid user id");
    }
    Card card = userDao.getCard(transaction.getCardNum());
    if (card == null) {
      throw new BuddyError("invalid card number");
    }
    Transaction result = transactionDao.createTransaction(transaction);
    if (result == null) {
      transactionDao.failTransaction(transaction);
      throw new BuddyError("transaction failed, please try again");
    }
    result.setCardNum(card.getCardNumber());
    Transaction fin = transactionDao.finalizeTransaction(result);
    if (fin == null) {
      transactionDao.failTransaction(transaction);
      throw new BuddyError("transaction failed, please try again");
    }
    return fin;
  }

  public List<Transaction> getUserTransactions(String emailId) throws BuddyError {
    User user = userDao.getUserDetail(emailId);
    if (user == null) {
      throw new BuddyError("invalid user id");
    }
    return transactionDao.getUserTransactions(emailId);
  }
}
