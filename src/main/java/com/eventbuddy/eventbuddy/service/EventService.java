package com.eventbuddy.eventbuddy.service;

import com.eventbuddy.eventbuddy.Utils.BuddyError;
import com.eventbuddy.eventbuddy.dao.EventDao;
import com.eventbuddy.eventbuddy.dao.OrgDao;
import com.eventbuddy.eventbuddy.dao.UserDao;
import com.eventbuddy.eventbuddy.model.Comment;
import com.eventbuddy.eventbuddy.model.Event;
import com.eventbuddy.eventbuddy.model.Image;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService {

  @Autowired
  private EventDao eventDao;
  @Autowired
  private OrgDao orgDao;
  @Autowired
  private UserDao userDao;

  public Event get(int eventId) throws BuddyError {
    return eventDao.getEvent(eventId);
  }

  public List<Event> getEvents() throws BuddyError {
    List<Event> events = eventDao.getEvents();
    if (events.isEmpty()) {
      throw new BuddyError("no events found");
    }
    return events;
  }

  public Event createEvent(Event request) throws BuddyError {
    if (orgDao.getOrg(request.getOrgId()) == null) {
      throw new BuddyError("Invalid Org for the event");
    }
    eventDao.createEvent(request);
    return request;
  }

  public Comment addComment(Comment request) throws BuddyError {
    if (eventDao.getEvent(request.getEventId()) == null) {
      throw new BuddyError("Event doesn't exist");
    }
    if (userDao.getUserDetail(request.getEmail()) == null) {
      throw new BuddyError("user doesn't exist");
    }
    eventDao.addComment(request);
    return request;
  }

  public List<Comment> getComment(int eventId) {
    return eventDao.getComment(eventId);
  }

  public Event updateEvent(Event request) throws BuddyError, IllegalAccessException {
    Event event = eventDao.getEvent(request.getEventId());
    if (event == null) {
      throw new BuddyError("invalid event Id");
    }
    event.update(request);
    eventDao.updateEvent(event);
    return event;
  }

  public Image addImage(int eventId, String imageUrl) throws BuddyError {
    Event event = eventDao.getEvent(eventId);
    if (event == null) {
      throw new BuddyError("invalid event Id");
    }
    Image result = eventDao.addImage(eventId, imageUrl);
    if (result == null) {
      throw new BuddyError("error in adding image, ");
    }
    return result;
  }

  public List<Image> getImages(int eventId) throws BuddyError {
    List<Image> result = eventDao.getImage(eventId);
    if (result.isEmpty()) {
      throw new BuddyError("error in getting event images");
    }
    return result;
  }

  public List<Event> getEventsByStatus(String status) throws BuddyError {
    return eventDao.getEventsByStatus(status.toUpperCase());
  }
}
