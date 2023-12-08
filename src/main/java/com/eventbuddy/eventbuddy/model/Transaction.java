package com.eventbuddy.eventbuddy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction {

  @JsonProperty("trans_id")
  private int transactionId;
  @JsonProperty("trans_datetime")
  private LocalDateTime time;

  @JsonProperty("trans_status")
  private String status;

  @JsonProperty("credit_card_num")
  private String cardNum;

  @JsonProperty("email_id")
  private String emailId;

  @JsonProperty("event_id")
  private int eventId;

  @JsonProperty("id_proof")
  private String id_proof;

  @JsonProperty("total_tickets")
  private int total_tickets;

  public LocalDateTime getTime() {
    return time;
  }

  public void setTime(LocalDateTime time) {
    this.time = time;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getCardNum() {
    return cardNum;
  }

  public void setCardNum(String cardNum) {
    this.cardNum = cardNum;
  }

  public String getEmailId() {
    return emailId;
  }

  public void setEmailId(String emailId) {
    this.emailId = emailId;
  }

  public int getEventId() {
    return eventId;
  }

  public void setEventId(int eventId) {
    this.eventId = eventId;
  }

  public String getId_proof() {
    return id_proof;
  }

  public void setId_proof(String id_proof) {
    this.id_proof = id_proof;
  }

  public float getTotalTickets() {
    return total_tickets;
  }

  public void setTotalTickets(int total_tickets) {
    this.total_tickets = total_tickets;
  }

  public int getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(int transactionId) {
    this.transactionId = transactionId;
  }
}
