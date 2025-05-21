package com.eventbuddy.eventbuddy.service;

import com.eventbuddy.eventbuddy.Utils.BuddyError;
import com.eventbuddy.eventbuddy.dao.UserDao;
import com.eventbuddy.eventbuddy.model.Address;
import com.eventbuddy.eventbuddy.model.Card;
import com.eventbuddy.eventbuddy.model.Ticket;
import com.eventbuddy.eventbuddy.model.User;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired
  private UserDao userDao;
  @Autowired
  private PasswordEncoder passwordEncoder;

  public User login(User user) throws BuddyError {
    User userCredential = userDao.getUserCredential(user.getEmail());
    if (userCredential == null || !passwordEncoder.matches(user.getPassword(),
        userCredential.getPassword())) {
      throw new BuddyError("Invalid User Credentials");
    } else {
      return userDao.getUserDetail(user.getEmail());
    }
  }

  public User register(User request) throws BuddyError {
    request.setPassword(passwordEncoder.encode(request.getPassword()));
    if (userDao.getUserDetail(request.getEmail()) != null) {
      throw new BuddyError("User already exists");
    }
    userDao.registerUser(request);
    User user = userDao.getUserDetail(request.getEmail());
    if (user == null) {
      throw new BuddyError("Registration failed, please check data");
    }
    return user;
  }

  public User get(String email) throws BuddyError {
    User user = userDao.getUserDetail(email);
    if (user == null) {
      throw new IllegalArgumentException("user not found");
    }
    return user;
  }

  public Card addCard(Card request) throws BuddyError {
    if (userDao.getUserDetail(request.getEmail()) == null) {
      throw new BuddyError("invalid user email");
    }
    userDao.addCard(request);
    Card card = userDao.getCard(request.getCardNumber());
    if (card == null) {
      throw new BuddyError("error in adding the card, please check data");
    }
    return card;
  }

  public void updatePassword(User user) throws BuddyError {
    if (userDao.getUserDetail(user.getEmail()) == null) {
      throw new BuddyError("invalid user email");
    }
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    userDao.updatePassword(user);
  }

  public Address addAddress(Address request) throws BuddyError {
    if (userDao.getUserDetail(request.getEmailId()) == null) {
      throw new BuddyError("invalid user email");
    }
    userDao.addAddress(request);
    Address address = userDao.addAddress(request);
    if (address == null) {
      throw new BuddyError("error in adding the card, please check dara");
    }
    return address;
  }

  public List<Address> getAddress(String email) throws BuddyError {
    if (userDao.getUserDetail(email) == null) {
      throw new BuddyError("invalid user email");
    }
    return userDao.getAddress(email);
  }

  public List<Card> getCard(String email) throws BuddyError {
    if (userDao.getUserDetail(email) == null) {
      throw new BuddyError("invalid user email");
    }
    return userDao.getCards(email);
  }

  public List<Ticket> getTickets(String emailId) throws BuddyError {
    return userDao.getTickets(emailId);
  }

  public boolean deleteUser(String emailId) throws BuddyError {
    if (userDao.getUserDetail(emailId) == null) {
      return true;
    }
    userDao.deleteAccount(emailId);
    return userDao.getUserDetail(emailId) == null;
  }
}
