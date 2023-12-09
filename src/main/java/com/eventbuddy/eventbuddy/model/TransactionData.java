package com.eventbuddy.eventbuddy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionData {
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

  @JsonProperty("ticket_id")
  private int ticketId;
  @JsonProperty("ticket_user_name")
  private String ticketUserName;
  @JsonProperty("event_name")
  private String eventName;

  @JsonProperty("email_id")
  private String email;
  @JsonProperty("first_name")
  private String firstName;
  @JsonProperty("last_name")
  private String lastName;
  @JsonProperty("date_of_birth")
  private Date dateOfBirth;

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

  public String getEventName() {
    return eventName;
  }

  public void setEventName(String eventName) {
    this.eventName = eventName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Date getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(Date dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }
}
