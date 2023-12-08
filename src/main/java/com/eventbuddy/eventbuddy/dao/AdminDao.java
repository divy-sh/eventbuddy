package com.eventbuddy.eventbuddy.dao;

import com.eventbuddy.eventbuddy.model.Ad;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AdminDao {

  @Autowired
  private QueryManager queryManager;

  public void approveEvent(String email, int eventId, String status) {
    String query = "call insert_event_approval(?, ?, ?, ?, ?)";
    queryManager.update(query, eventId, new Date(), status.toUpperCase(), "", email);
  }

  public void approveAd(String email, int adId, String status) {
    String query = "call insert_ad_approval(?, ?, ?, ?, ?)";
    queryManager.update(query, adId, new Date(), status.toUpperCase(), "", email);
  }
}
