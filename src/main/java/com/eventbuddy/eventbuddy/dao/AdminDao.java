package com.eventbuddy.eventbuddy.dao;

import com.eventbuddy.eventbuddy.Utils.BuddyError;
import com.eventbuddy.eventbuddy.model.Ad;
import com.eventbuddy.eventbuddy.model.TransactionData;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AdminDao {

  @Autowired
  private QueryManager queryManager;

  public void createEventApproval(String email, int eventId, String status) throws BuddyError {
    String query = "call insert_event_approval(?, ?, ?, ?, ?)";
    queryManager.update(query, eventId, new Date(), status.toUpperCase(), "", email);
  }

  public void updateEventApproval(String email, int eventId, String status) throws BuddyError {
    String query = "call update_event_approval(?, ?, ?, ?, ?)";
    queryManager.update(query, eventId, new Date(), status.toUpperCase(), "", email);
  }

  public void approveAd(String email, int adId, String status) throws BuddyError {
    String query = "call insert_ad_approval(?, ?, ?, ?, ?)";
    queryManager.update(query, adId, new Date(), status.toUpperCase(), "", email);
  }

  public List<TransactionData> getTransactionData() throws BuddyError {
    String query = "call get_transaction_data()";
    return queryManager.runQuery(query, TransactionData.class);
  }
}
