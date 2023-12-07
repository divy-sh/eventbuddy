package com.eventbuddy.eventbuddy.service;

import com.eventbuddy.eventbuddy.Utils.BuddyError;
import com.eventbuddy.eventbuddy.dao.EventDao;
import com.eventbuddy.eventbuddy.dao.TransactionDao;
import com.eventbuddy.eventbuddy.dao.UserDao;
import com.eventbuddy.eventbuddy.model.Card;
import com.eventbuddy.eventbuddy.model.Event;
import com.eventbuddy.eventbuddy.model.Ticket;
import com.eventbuddy.eventbuddy.model.Transaction;
import com.eventbuddy.eventbuddy.model.User;
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

  public Ticket buyTicket(Transaction transaction) throws BuddyError {
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
    transaction.setStatus("INPROGRESS");
    Transaction result = transactionDao.createTransaction(transaction);
    if (result == null) {
      throw new BuddyError("transaction failed, please try again");
    }
    result.setStatus("COMPLETE");
    result = transactionDao.updateTransaction(result);
    return transactionDao.generateTicket(result);
  }
}
