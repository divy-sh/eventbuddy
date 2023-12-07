package com.eventbuddy.eventbuddy.controller;

import com.eventbuddy.eventbuddy.Utils.BuddyError;
import com.eventbuddy.eventbuddy.Utils.ErrorResponse;
import com.eventbuddy.eventbuddy.model.Org;
import com.eventbuddy.eventbuddy.service.OrgService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping("org")
public class OrgController {

  @Autowired
  private OrgService orgService;

  @GetMapping(value = "get", produces = "application/json")
  public ResponseEntity<?> get(@RequestParam("orgId") int orgId) {
    try {
      Org org = orgService.get(orgId);
      return ResponseEntity.ok(org);
    } catch (IllegalArgumentException | BuddyError e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e));
    }
  }

  @GetMapping(value = "get/all", produces = "application/json")
  public ResponseEntity<?> getOrgs(@RequestParam("email") String email) {
    try {
      List<Org> orgs = orgService.getOrgs(email);
      return ResponseEntity.ok(orgs);
    } catch (IllegalArgumentException | BuddyError e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e));
    }
  }

  @PostMapping(value = "create", produces = "application/json")
  public ResponseEntity<?> createOrg(@RequestBody Org org, @RequestParam("email") String email) {
    try {
      Org result = orgService.createOrg(org, email);
      return ResponseEntity.ok(result);
    } catch (IllegalArgumentException | BuddyError e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e));
    }
  }

  @GetMapping(value = "add/team", produces = "application/json")
  public ResponseEntity<?> addUserToOrg(@RequestParam("org_id") int orgId,
      @RequestParam("email") String email) {
    try {
      orgService.addUserToOrg(orgId, email);
      return ResponseEntity.ok().build();
    } catch (IllegalArgumentException | BuddyError e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e));
    }
  }
}
