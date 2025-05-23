package com.eventbuddy.eventbuddy.controller;

import com.eventbuddy.eventbuddy.Utils.BuddyError;
import com.eventbuddy.eventbuddy.Utils.ErrorResponse;
import com.eventbuddy.eventbuddy.model.Ad;
import com.eventbuddy.eventbuddy.service.AdService;
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
@RequestMapping(value = "ad")
public class AdController {

  @Autowired
  private AdService adService;

  @PostMapping(value = "create", produces = "application/json")
  public ResponseEntity<?> createAd(@RequestBody Ad ad) {
    try {
      Ad result = adService.createAd(ad);
      return ResponseEntity.ok(result);
    } catch (IllegalArgumentException | BuddyError e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e));
    }
  }

  @GetMapping(value = "get", produces = "application/json")
  public ResponseEntity<?> get(@RequestParam String status) {
    try {
      Ad ad = adService.getRandomAd(status.toUpperCase());
      return ResponseEntity.ok(ad);
    } catch (IllegalArgumentException | BuddyError e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e));
    }
  }

  @GetMapping(value = "get/all/status", produces = "application/json")
  public ResponseEntity<?> getByStatus(@RequestParam("approval") String status) {
    try {
      List<Ad> ad = adService.getAdWithFilter(status);
      return ResponseEntity.ok(ad);
    } catch (IllegalArgumentException | BuddyError e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e));
    }
  }

  @GetMapping(value = "get/all/byOrg", produces = "application/json")
  public ResponseEntity<?> getAllByOrg(@RequestParam String status, @RequestParam("org_id") int orgId) {
    try {
      List<Ad> ad = adService.getAllByOrg(status, orgId);
      return ResponseEntity.ok(ad);
    } catch (IllegalArgumentException | BuddyError e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e));
    }
  }

  @DeleteMapping(value = "delete", produces = "application/json")
  public ResponseEntity<?> delete(@RequestParam("ad_id") int adId) {
    try {
      if (adService.delete(adId)) {
        return ResponseEntity.ok().build();
      } else {
        throw new BuddyError("error in deleting ad");
      }
    } catch (IllegalArgumentException | BuddyError e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e));
    }
  }

  @PostMapping(value = "update", produces = "application/json")
  public ResponseEntity<?> update(@RequestBody Ad ad) {
    try {
      Ad result = adService.update(ad);
      return ResponseEntity.ok(result);
    } catch (IllegalArgumentException | BuddyError e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e));
    }
  }
}
