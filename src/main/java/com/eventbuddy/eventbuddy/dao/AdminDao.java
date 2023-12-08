package com.eventbuddy.eventbuddy.dao;

import com.eventbuddy.eventbuddy.model.Ad;
import java.util.List;
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

  public Ad approveAd(String email, int adId) {
    String query = "call insert_ad_approval(?, ?)";
    List<Ad> ad = queryManager.runQuery(query, Ad.class, adId, email);
    if (ad.isEmpty()) {
      return null;
    }
    return ad.get(0);
  }
}
