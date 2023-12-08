package com.eventbuddy.eventbuddy.controller;

import com.eventbuddy.eventbuddy.Utils.BuddyError;
import com.eventbuddy.eventbuddy.Utils.ErrorResponse;
import com.eventbuddy.eventbuddy.model.Comment;
import com.eventbuddy.eventbuddy.model.Event;
import com.eventbuddy.eventbuddy.model.Image;
import com.eventbuddy.eventbuddy.service.EventService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "event")
public class EventController {

  @Autowired
  private EventService eventService;

  @GetMapping(value = "get", produces = "application/json")
  public ResponseEntity<?> getEvent(@RequestParam("event_id") int eventId) {
    try {
      Event event = eventService.get(eventId);
      return ResponseEntity.ok(event);
    } catch (IllegalArgumentException | BuddyError e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e));
    }
  }

  @GetMapping(value = "get/all", produces = "application/json")
  public ResponseEntity<?> getEventsByStatus(@RequestParam("status") String approval) {
    try {
      List<Event> events = eventService.getEventsByStatus(approval);
      return ResponseEntity.ok(events);
    } catch (IllegalArgumentException | BuddyError e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e));
    }
  }

  @PostMapping(value = "create", produces = "application/json")
  public ResponseEntity<?> createEvent(@RequestBody Event request) {
    try {
      Event event = eventService.createEvent(request);
      return ResponseEntity.ok(event);
    } catch (IllegalArgumentException | BuddyError e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e));
    }
  }

  @PostMapping(value = "update", produces = "application/json")
  public ResponseEntity<?> updateEvent(@RequestBody Event request) {
    try {
      Event event = eventService.updateEvent(request);
      return ResponseEntity.ok(event);
    } catch (IllegalArgumentException | BuddyError | IllegalAccessException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e));
    }
  }

  @PostMapping(value = "add/comment", produces = "application/json")
  public ResponseEntity<?> addComment(@RequestBody Comment request) {
    try {
      Comment comment = eventService.addComment(request);
      return ResponseEntity.ok(comment);
    } catch (IllegalArgumentException | BuddyError e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e));
    }
  }

  @GetMapping(value = "get/comment", produces = "application/json")
  public ResponseEntity<?> getComment(@RequestParam("eventId") int eventId) {
    try {
      List<Comment> comment = eventService.getComment(eventId);
      return ResponseEntity.ok(comment);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e));
    }
  }

  @GetMapping(value = "add/image", produces = "application/json")
  public ResponseEntity<?> addImage(@RequestParam("eventId") int eventId,
      @RequestParam("imageUrl") String imageUrl) {
    try {
      Image image = eventService.addImage(eventId, imageUrl);
      return ResponseEntity.ok(image);
    } catch (IllegalArgumentException | BuddyError e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e));
    }
  }

  @GetMapping(value = "get/image", produces = "application/json")
  public ResponseEntity<?> getImage(@RequestParam("eventId") int eventId) {
    try {
      List<Image> image = eventService.getImages(eventId);
      return ResponseEntity.ok(image);
    } catch (IllegalArgumentException | BuddyError e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e));
    }
  }
}
