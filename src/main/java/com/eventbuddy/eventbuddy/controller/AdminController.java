package com.eventbuddy.eventbuddy.controller;

import com.eventbuddy.eventbuddy.Utils.BuddyError;
import com.eventbuddy.eventbuddy.Utils.ErrorResponse;
import com.eventbuddy.eventbuddy.model.Ad;
import com.eventbuddy.eventbuddy.model.Event;
import com.eventbuddy.eventbuddy.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "admin")
public class AdminController {

  @Autowired
  private AdminService adminService;

  @GetMapping(value = "approve/event", produces = "application/json")
  public ResponseEntity<?> approveEvent(@RequestParam("email") String email,
      @RequestParam("event_id") int eventId, @RequestParam("status") String status) {
    try {
      Event event = adminService.approveEvent(email, eventId, status);
      return ResponseEntity.ok(event);
    } catch (IllegalArgumentException | BuddyError e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e));
    }
  }

  @GetMapping(value = "approve/ad", produces = "application/json")
  public ResponseEntity<?> approveAd(@RequestParam("email") String email,
      @RequestParam("ad_id") int adId, @RequestParam("status") String status) {
    try {
      adminService.approveAd(email, adId, status);
      return ResponseEntity.ok().build();
    } catch (IllegalArgumentException | BuddyError e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e));
    }
  }
}
