package com.eventbuddy.eventbuddy.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AdminDao {

  @Autowired
  private QueryManager queryManager;

  public void approveEvent(String email, int eventId) {
    String query = "call insert_event_approval(?, ?)";
    queryManager.update(query, eventId, email);
  }
}
