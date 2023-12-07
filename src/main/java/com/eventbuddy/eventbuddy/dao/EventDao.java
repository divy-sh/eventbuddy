package com.eventbuddy.eventbuddy.dao;

import com.eventbuddy.eventbuddy.Utils.BuddyError;
import com.eventbuddy.eventbuddy.model.Comment;
import com.eventbuddy.eventbuddy.model.Event;
import com.eventbuddy.eventbuddy.model.Image;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EventDao {

  @Autowired
  private JdbcTemplate jdbcTemplate;
  @Autowired
  private QueryManager queryManager;

  public Event getEvent(int eventId) throws BuddyError {
    String query = "call get_event_record(?)";
    List<Event> events = queryManager.runQuery(query, Event.class, eventId);
    if (events.isEmpty()) {
      return null;
    }
    return events.get(0);
  }

  public List<Event> getEvents() {
    String query = "call get_all_events()";
    return queryManager.runQuery(query, Event.class);
  }

  public List<Event> getApprovedEvents() {
    String query = "call get_all_approved_events()";
    return queryManager.runQuery(query, Event.class);
  }

  public void createEvent(Event event) {
    String query = "call insert_event_record(?, ?, ?, ?, ?, ?, ?, ?)";
    queryManager.update(query, event.getEventName(), event.getEventDescription(),
        event.getEventStart(), event.getEventEnd(), event.getLastRegistrationDate(),
        event.getCapacity(), event.getEntryFee(), event.getOrgId());
  }

  public void addComment(Comment request) {
    String query = "call insert_event_comment(?, ?, ?, ?)";
    queryManager.update(query, request.getCommentText(), request.getTime(), request.getEmail(),
        request.getEventId());
  }

  public List<Comment> getComment(int eventId) {
    String query = "call get_event_comment(?)";
    return queryManager.runQuery(query, Comment.class, eventId);
  }

  public void updateEvent(Event event) {
    String query = "call update_event_record(?, ?, ?, ?, ?, ?, ?, ?)";
    queryManager.update(query, event.getEventName(), event.getEventDescription(),
        event.getEventStart(), event.getEventEnd(), event.getLastRegistrationDate(),
        event.getCapacity(), event.getEntryFee(), event.getOrgId());
  }

  public List<Image> getImage(int eventId) {
    String query = "call get_event_images(?)";
    return queryManager.runQuery(query, Image.class, eventId);
  }

  public Image addImage(int eventId, String imageUrl) {
    String query = "call insert_event_image(?, ?)";
    List<Image> images = queryManager.runQuery(query, Image.class, imageUrl, eventId);
    if(images.isEmpty()) {
      return null;
    }
    return images.get(0);
  }
}
