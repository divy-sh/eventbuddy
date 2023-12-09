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
  private String idProof;

  @JsonProperty("total_tickets")
  private int totalTickets;

  public int getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(int transactionId) {
    this.transactionId = transactionId;
  }

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

  public String getIdProof() {
    return idProof;
  }

  public void setIdProof(String idProof) {
    this.idProof = idProof;
  }

  public int getTotalTickets() {
    return totalTickets;
  }

  public void setTotalTickets(int totalTickets) {
    this.totalTickets = totalTickets;
  }
}
