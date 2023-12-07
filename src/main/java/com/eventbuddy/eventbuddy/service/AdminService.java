package com.eventbuddy.eventbuddy.service;

import com.eventbuddy.eventbuddy.Utils.BuddyError;
import com.eventbuddy.eventbuddy.dao.AdminDao;
import com.eventbuddy.eventbuddy.dao.EventDao;
import com.eventbuddy.eventbuddy.dao.UserDao;
import com.eventbuddy.eventbuddy.model.Event;
import com.eventbuddy.eventbuddy.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

  @Autowired
  private AdminDao adminDao;

  @Autowired
  private UserDao userDao;

  @Autowired
  private EventDao eventDao;

  public Event approveEvent(String email, int eventId) throws BuddyError {
    User admin = userDao.getUserDetail(email);
    if (admin == null || !admin.isAdmin()) {
      throw new BuddyError("Invalid user");
    }
    Event event = eventDao.getEvent(eventId);
    if (event == null) {
      throw new BuddyError("Invalid event");
    }
    adminDao.approveEvent(email, eventId);
    return event;
  }
}
