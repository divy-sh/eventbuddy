package com.eventbuddy.eventbuddy.service;

import com.eventbuddy.eventbuddy.Utils.BuddyError;
import com.eventbuddy.eventbuddy.Utils.Utils;
import com.eventbuddy.eventbuddy.dao.AdminDao;
import com.eventbuddy.eventbuddy.dao.EventDao;
import com.eventbuddy.eventbuddy.model.Event;
import com.eventbuddy.eventbuddy.model.TransactionData;
import com.eventbuddy.eventbuddy.model.User;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

  @Autowired
  private AdminDao adminDao;

  @Autowired
  private EventDao eventDao;

  public Event approveEvent(int eventId, String status) throws BuddyError {
    User admin = Utils.getAuthenticatedUser();
    Event event = eventDao.getEvent(eventId);
    if (event == null) {
      throw new BuddyError("Invalid event");
    }
    adminDao.updateEventApproval(admin.getEmail(), eventId, status);
    event = eventDao.getEvent(eventId);
    return event;
  }

  public void approveAd(int adId, String status) throws BuddyError {
    User admin = Utils.getAuthenticatedUser();
    adminDao.approveAd(admin.getEmail(), adId, status);
  }

  public List<TransactionData> getTransactionData() throws BuddyError {
    return adminDao.getTransactionData();
  }
}
