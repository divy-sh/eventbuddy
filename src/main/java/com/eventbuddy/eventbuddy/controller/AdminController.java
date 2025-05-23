package com.eventbuddy.eventbuddy.controller;

import com.eventbuddy.eventbuddy.Utils.BuddyError;
import com.eventbuddy.eventbuddy.Utils.ErrorResponse;
import com.eventbuddy.eventbuddy.model.Event;
import com.eventbuddy.eventbuddy.model.TransactionData;
import com.eventbuddy.eventbuddy.service.AdminService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

  @Autowired
  private AdminService adminService;

  @GetMapping(value = "approve/event", produces = "application/json")
  public ResponseEntity<?> approveEvent(@RequestParam("event_id") int eventId, @RequestParam String status) {
    try {
      Event event = adminService.approveEvent(eventId, status);
      return ResponseEntity.ok(event);
    } catch (IllegalArgumentException | BuddyError e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e));
    }
  }

  @GetMapping(value = "approve/ad", produces = "application/json")
  public ResponseEntity<?> approveAd(@RequestParam("ad_id") int adId, @RequestParam String status) {
    try {
      adminService.approveAd(adId, status);
      return ResponseEntity.ok().build();
    } catch (IllegalArgumentException | BuddyError e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e));
    }
  }

  @GetMapping(value = "get/transaction/data", produces = "application/json")
  public ResponseEntity<?> getTransactionData() {
    try {
      List<TransactionData> td = adminService.getTransactionData();
      return ResponseEntity.ok(td);
    } catch (IllegalArgumentException | BuddyError e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e));
    }
  }
}
