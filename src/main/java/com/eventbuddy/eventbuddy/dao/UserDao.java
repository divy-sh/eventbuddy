package com.eventbuddy.eventbuddy.dao;

import com.eventbuddy.eventbuddy.Utils.BuddyError;
import com.eventbuddy.eventbuddy.model.Address;
import com.eventbuddy.eventbuddy.model.Card;
import com.eventbuddy.eventbuddy.model.Ticket;
import com.eventbuddy.eventbuddy.model.User;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {

  @Autowired
  private QueryManager queryManager;

  public User getUserDetail(String email) throws BuddyError {
    String query = "call get_user_detail(?)";
    List<User> users = queryManager.runQuery(query, User.class, email);
    if (users.isEmpty()) {
      return null;
    }
    return users.getFirst();
  }

  public User getUserCredential(String email) throws BuddyError {
    String query = "call get_user_credential(?)";
    List<User> users = queryManager.runQuery(query, User.class, email);
    if (users.isEmpty()) {
      return null;
    }
    return users.getFirst();
  }

  public void registerUser(User user) {
    String query = "call insert_user(?, ?, ?, ?, ?, ?, ?, ?, ?)";
    queryManager.update(query, user.getEmail(), user.getPassword(),
        user.getFirstName(), user.getLastName(), user.getDateOfBirth(), null, null, false,
        user.isOrganizer());
  }

  public void addCard(Card card) {
    String query = "call insert_credit_card(?, ?, ?, ?)";
    queryManager.update(query, card.getCardNumber(), card.getExpiryDate(), card.getName(),
        card.getEmail());
  }

  public Card getCard(String cardNumber) throws BuddyError {
    String query = "call get_cc_num(?)";
    List<Card> cards = queryManager.runQuery(query, Card.class, cardNumber);
    if (cards.isEmpty()) {
      return null;
    }
    return cards.getFirst();
  }

  public List<Card> getCards(String email) throws BuddyError {
    String query = "call get_cc_email_id(?)";
    return queryManager.runQuery(query, Card.class, email);
  }

  public void updatePassword(User user) {
    String query = "call update_user_credential(?, ?)";
    queryManager.update(query, user.getEmail(), user.getPassword());
  }

  public Address addAddress(Address request) throws BuddyError {
    String query = "call insert_user_address(?, ?, ?, ?, ?, ?, ?)";
    List<Address> addresses = queryManager.runQuery(query, Address.class, request.getEmailId(),
        request.getAddressLine1(), request.getAddressLine2(), request.getCity(), request.getState(),
        request.getZipCode(), request.getCountry());
    if (addresses.isEmpty()) {
      return null;
    }
    return addresses.getFirst();
  }

  public List<Address> getAddress(String emailId) throws BuddyError {
    String query = "call get_user_address(?)";
    return queryManager.runQuery(query, Address.class, emailId);
  }

  public List<Ticket> getTickets(String emailId) throws BuddyError {
    String query = "call get_user_ticket(?)";
    return queryManager.runQuery(query, Ticket.class, emailId);
  }

  public void deleteAccount(String emailId) {
    String query = "call delete_user_credential(?)";
    queryManager.update(query, emailId);
  }
}
