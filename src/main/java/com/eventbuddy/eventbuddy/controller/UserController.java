package com.eventbuddy.eventbuddy.controller;

import com.eventbuddy.eventbuddy.Utils.BuddyError;
import com.eventbuddy.eventbuddy.Utils.ErrorResponse;
import com.eventbuddy.eventbuddy.model.Address;
import com.eventbuddy.eventbuddy.model.Card;
import com.eventbuddy.eventbuddy.model.Ticket;
import com.eventbuddy.eventbuddy.model.User;
import com.eventbuddy.eventbuddy.model.UserToken;
import com.eventbuddy.eventbuddy.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "user")
public class UserController {

  @Autowired
  UserService userService;

  @PostMapping(value = "login", produces = "application/json")
  public ResponseEntity<?> login(@RequestBody User request) {
    try {
      UserToken token = userService.login(request);
      return ResponseEntity.ok(token);
    } catch (IllegalArgumentException | BuddyError e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e));
    }
  }

  @PostMapping(value = "register", produces = "application/json")
  public ResponseEntity<?> register(@RequestBody User request) {
    try {
      User user = userService.register(request);
      return ResponseEntity.ok(user);
    } catch (IllegalArgumentException | BuddyError e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e));
    }
  }

  @GetMapping(value = "get", produces = "application/json")
  public ResponseEntity<?> get(@RequestParam("email") String email) {
    try {
      User user = userService.get(email);
      return ResponseEntity.ok(user);
    } catch (IllegalArgumentException | BuddyError e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e));
    }
  }

  @PostMapping(value = "add/card", produces = "application/json")
  public ResponseEntity<?> addUserCard(@RequestBody Card card) {
    try {
      Card result = userService.addCard(card);
      return ResponseEntity.ok(result);
    } catch (IllegalArgumentException | BuddyError e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e));
    }
  }

  @GetMapping(value = "get/card", produces = "application/json")
  public ResponseEntity<?> getUserCard(@RequestParam("email") String email) {
    try {
      List<Card> result = userService.getCard(email);
      return ResponseEntity.ok(result);
    } catch (IllegalArgumentException | BuddyError e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e));
    }
  }

  @PostMapping(value = "add/address", produces = "application/json")
  public ResponseEntity<?> addAddress(@RequestBody Address request) {
    try {
      Address result = userService.addAddress(request);
      return ResponseEntity.ok(result);
    } catch (IllegalArgumentException | BuddyError e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e));
    }
  }

  @GetMapping(value = "get/address", produces = "application/json")
  public ResponseEntity<?> getUserAddress(@RequestParam("email") String email) {
    try {
      List<Address> result = userService.getAddress(email);
      return ResponseEntity.ok(result);
    } catch (IllegalArgumentException | BuddyError e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e));
    }
  }

  @PostMapping(value = "update/password", produces = "application/json")
  public ResponseEntity<?> updatePassword(@RequestBody User user) {
    try {
      userService.updatePassword(user);
      return ResponseEntity.ok().build();
    } catch (IllegalArgumentException | BuddyError e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e));
    }
  }

  @DeleteMapping(value = "delete", produces = "application/json")
  public ResponseEntity<?> deleteUser(@RequestParam("email_id") String emailId) {
    try {
      if (userService.deleteUser(emailId)) {
        return ResponseEntity.ok().build();
      } else {
        throw new BuddyError("user deletion failed");
      }
    } catch (IllegalArgumentException | BuddyError e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e));
    }
  }

  @GetMapping(value = "get/ticket", produces = "application/json")
  public ResponseEntity<?> getTickets(@RequestParam("email_id") String emailId) {
    try {
      List<Ticket> tickets = userService.getTickets(emailId);
      return ResponseEntity.ok(tickets);
    } catch (IllegalArgumentException | BuddyError e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e));
    }
  }
}
