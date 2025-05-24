package com.eventbuddy.eventbuddy.service;

import com.eventbuddy.eventbuddy.Utils.BuddyError;
import com.eventbuddy.eventbuddy.Utils.Utils;
import com.eventbuddy.eventbuddy.dao.EventDao;
import com.eventbuddy.eventbuddy.dao.OrgDao;
import com.eventbuddy.eventbuddy.dao.UserDao;
import com.eventbuddy.eventbuddy.model.Comment;
import com.eventbuddy.eventbuddy.model.Event;
import com.eventbuddy.eventbuddy.model.Image;
import com.eventbuddy.eventbuddy.model.Org;
import com.eventbuddy.eventbuddy.model.OrgTeam;
import com.eventbuddy.eventbuddy.model.User;

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
    Org org = orgDao.getOrg(request.getOrgId());
    if (org == null) {
      throw new BuddyError("Invalid Org for the event");
    }

    // check if current auth user is part of the org passed, if not fail the request
    authUserHasAccessToOrg(org.getOrgId());
    
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

  public List<Comment> getComment(int eventId) throws BuddyError {
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
    Org org = orgDao.getOrg(event.getOrgId());

    // check if current auth user is part of the org passed, if not fail the request
    authUserHasAccessToOrg(org.getOrgId());

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

  public List<Event> getEventsByStatusAndOrg(String approval, int orgId) throws BuddyError {
    return eventDao.getEventsByStatusAndOrg(approval.toUpperCase(), orgId);
  }

  private void authUserHasAccessToOrg(int orgId) throws BuddyError {
    User authUser = Utils.getAuthenticatedUser();
    if (authUser == null) {
      throw new BuddyError("No Authentication");
    }
    List<OrgTeam> orgTeam = orgDao.getOrgTeam(orgId);
    boolean isUserInOrgTeam = orgTeam.stream()
    .anyMatch(teamMember -> teamMember.getOrganizerEmailId().equals(authUser.getEmail()));
    if (!isUserInOrgTeam) {
        throw new BuddyError("Authenticated user is not a member of the organization");
    }
  }
}
