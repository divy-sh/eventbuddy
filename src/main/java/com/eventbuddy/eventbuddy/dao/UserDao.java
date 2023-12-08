package com.eventbuddy.eventbuddy.dao;

import com.eventbuddy.eventbuddy.Utils.BuddyError;
import com.eventbuddy.eventbuddy.model.Address;
import com.eventbuddy.eventbuddy.model.Card;
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
    return users.get(0);
  }

  public User getUserCredential(String email) throws BuddyError {
    String query = "call get_user_credential(?)";
    List<User> users = queryManager.runQuery(query, User.class, email);
    if (users.isEmpty()) {
      return null;
    }
    return users.get(0);
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
        card.getExpiryDate());
  }

  public Card getCard(String cardNumber) {
    String query = "call get_cc_num(?)";
    List<Card> cards = queryManager.runQuery(query, Card.class, cardNumber);
    if (cards.isEmpty()) {
      return null;
    }
    return cards.get(0);
  }

  public List<Card> getCards(String email) {
    String query = "call get_credit_card_by_email(?)";
    return queryManager.runQuery(query, Card.class, email);
  }

  public void updatePassword(User user) {
    String query = "call get_credit_card_by_email(?)";
    queryManager.update(query, user.getPassword(), user.getEmail());
  }

  public Address addAddress(Address request) {
    String query = "call insert_user_address(?, ?, ?, ?, ?, ?, ?)";
    List<Address> addresses = queryManager.runQuery(query, Address.class, request.getEmailId(),
        request.getAddressLine1(), request.getAddressLine2(), request.getCity(), request.getState(),
        request.getZipCode(), request.getCountry());
    if (addresses.isEmpty()) {
      return null;
    }
    return addresses.get(0);
  }

  public List<Address> getAddress(String emailId) {
    String query = "call get_user_address(?)";
    return queryManager.runQuery(query, Address.class, emailId);
  }
}
