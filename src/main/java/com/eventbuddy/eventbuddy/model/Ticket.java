package com.eventbuddy.eventbuddy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Ticket {

  @JsonProperty("ticket_id")
  private int ticketId;
  @JsonProperty("ticket_user_name")
  private String ticketUserName;
  @JsonProperty("id_proof")
  private String idProof;
  @JsonProperty("trans_id")
  private String transactionId;
  
  public int getTicketId() {
    return ticketId;
  }
  public void setTicketId(int ticketId) {
    this.ticketId = ticketId;
  }
  public String getTicketUserName() {
    return ticketUserName;
  }
  public void setTicketUserName(String ticketUserName) {
    this.ticketUserName = ticketUserName;
  }
  public String getIdProof() {
    return idProof;
  }
  public void setIdProof(String idProof) {
    this.idProof = idProof;
  }
  public String getTransactionId() {
    return transactionId;
  }
  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }
}
